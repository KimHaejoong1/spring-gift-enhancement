package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.auth.jwt.JwtUtil;
import gift.common.code.CustomResponseCode;
import gift.common.dto.CustomResponseBody;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    private RestClient client;

    @BeforeEach
    void setup() {
        String token = jwtUtil.generateToken("test@domain.com", 1L);
        String url = "http://localhost:" + port + "/api/products";

        this.client = RestClient.builder()
            .baseUrl(url)
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }

    @Test
    @DisplayName("상품 생성 테스트")
    void testCreateProduct() {
        ProductRequest request = new ProductRequest("테스트 상품", 4500, "https://test.jpg");

        CustomResponseBody<ProductResponse> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(new ParameterizedTypeReference<CustomResponseBody<ProductResponse>>() {
            });

        assertResponse(response, CustomResponseCode.CREATED);

        ProductResponse data = response.data();

        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data.name()).isEqualTo("테스트 상품"),
            () -> assertThat(data.price()).isEqualTo(4500),
            () -> assertThat(data.imageUrl()).isEqualTo("https://test.jpg")
        );
    }

    @Test
    @DisplayName("상품 조회 테스트")
    void testGetProduct() {
        String name = "테스트 조회 상품";
        int price = 4500;
        String imageUrl = "https://test.jpg";

        Long id = createSampleProduct(name, price, imageUrl);

        CustomResponseBody<ProductResponse> response = client.get()
            .uri("/{id}", id)
            .retrieve()
            .body(new ParameterizedTypeReference<CustomResponseBody<ProductResponse>>() {
            });

        assertResponse(response, CustomResponseCode.RETRIEVED);

        ProductResponse data = response.data();

        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data.id()).isEqualTo(id),
            () -> assertThat(data.name()).isEqualTo(name),
            () -> assertThat(data.price()).isEqualTo(price),
            () -> assertThat(data.imageUrl()).isEqualTo(imageUrl)
        );
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void testGetProductList() {
        createSampleProduct("테스트 상품1", 3000, "https://test1.jpg");
        createSampleProduct("테스트 상품2", 3500, "https://test2.jpg");

        CustomResponseBody<List<ProductResponse>> response = client.get()
            .uri("")
            .retrieve()
            .body(new ParameterizedTypeReference<CustomResponseBody<List<ProductResponse>>>() {
            });

        assertResponse(response, CustomResponseCode.LIST_RETRIEVED);

        List<ProductResponse> data = response.data();

        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data).anyMatch(p -> p.name().equals("테스트 상품1")),
            () -> assertThat(data).anyMatch(p -> p.name().equals("테스트 상품2"))
        );
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void testUpdateProduct() {
        Long id = createSampleProduct("테스트 기존 상품", 1000, "https://old.jpg");

        ProductRequest update = new ProductRequest("테스트 수정 상품", 1500, "https://new.jpg");

        CustomResponseBody<ProductResponse> response = client.put()
            .uri("/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(update)
            .retrieve()
            .body(new ParameterizedTypeReference<CustomResponseBody<ProductResponse>>() {
            });

        assertResponse(response, CustomResponseCode.UPDATED);

        ProductResponse data = response.data();

        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data.id()).isEqualTo(id),
            () -> assertThat(data.name()).isEqualTo("테스트 수정 상품"),
            () -> assertThat(data.price()).isEqualTo(1500),
            () -> assertThat(data.imageUrl()).isEqualTo("https://new.jpg")
        );
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void testDeleteProduct() {
        Long id = createSampleProduct("테스트 삭제 상품", 2000, "https://test.jpg");

        ResponseEntity<Void> response = client.delete()
            .uri("/{id}", id)
            .retrieve()
            .toBodilessEntity();

        assertAll("응답 객체 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getStatusCode().value())
                .isEqualTo(CustomResponseCode.DELETED.getHttpStatus().value())
        );
    }

    @Test
    @DisplayName("상품명 빈 값 유효성 검사")
    void testProductNameBlankValidation() {
        ProductRequest invalidRequest = new ProductRequest("", 1000, "https://test.jpg");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "상품명은 필수입니다.");
    }

    @Test
    @DisplayName("상품명 최대 길이 유효성 검사")
    void testProductNameLengthValidation() {
        ProductRequest invalidRequest = new ProductRequest("일이삼사오육칠팔구십123456", 1000,
            "https://test.jpg");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "상품명은 15자 까지만 입력 가능합니다.");
    }

    @Test
    @DisplayName("상품명 허용되지 않는 문자 유효성 검사")
    void testProductNamePatternValidation() {
        ProductRequest invalidRequest = new ProductRequest("@@@!!!", 1000, "https://test.jpg");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "지원하지 않는 문자가 포함되어있습니다.");
    }

    @Test
    @DisplayName("상품명에 카카오 포함 시 유효성 검사")
    void testProductNameForbiddenKeywordValidation() {
        ProductRequest invalidRequest = new ProductRequest("카카오", 3000, "https://test.jpg");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response,
            String.format(CustomResponseCode.FORBIDDEN_KEYWORD.getMessage(), "카카오"));
    }

    @Test
    @DisplayName("가격 누락 유효성 검사")
    void testPriceRequiredValidation() {
        ProductRequest invalidRequest = new ProductRequest("테스트 상품", null, "https://test.jpg");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "가격은 필수입니다.");
    }

    @Test
    @DisplayName("이미지 URL 누락 유효성 검사")
    void testImageUrlRequiredValidation() {
        ProductRequest invalidRequest = new ProductRequest("테스트 상품", 1000, "");

        ResponseEntity<String> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "이미지 URL은 필수입니다.");
    }

    private Long createSampleProduct(String name, int price, String imageUrl) {
        ProductRequest request = new ProductRequest(name, price, imageUrl);

        CustomResponseBody<ProductResponse> response = client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(new ParameterizedTypeReference<CustomResponseBody<ProductResponse>>() {
            });

        return response.data().id();
    }

    private <T> void assertResponse(CustomResponseBody<T> response,
        CustomResponseCode expectedCode) {
        assertAll("응답 객체 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.status()).isEqualTo(expectedCode.getCode()),
            () -> assertThat(response.data()).isNotNull()
        );
    }

    private void assertValidationError(ResponseEntity<String> response, String expectedMessage) {
        assertAll("응답 객체 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getStatusCode().value())
                .isEqualTo(CustomResponseCode.VALIDATION_FAILED.getHttpStatus().value()),
            () -> assertThat(response.getBody()).contains(expectedMessage)
        );
    }
}
