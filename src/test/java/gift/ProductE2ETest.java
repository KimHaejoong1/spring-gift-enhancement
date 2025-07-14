package gift;

import gift.dto.*;
import gift.utils.E2ETestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductE2ETest {

    @LocalServerPort
    private int port;

    private RestClient restClient;
    private String token;
    private ProductResponseDto product;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        token = new E2ETestUtils(restClient).회원가입_후_토큰_발급();

        ProductRequestDto request = new ProductRequestDto("테스트 상품", 5000, "test.jpg");

        restClient.post()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        ProductResponseDto[] products = restClient.get()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(ProductResponseDto[].class);

        product = products[products.length - 1];
    }

    @Test
    void 상품을_등록하고_조회() {
        ProductRequestDto request = new ProductRequestDto("녹차", 3500, "green_tea.jpg");

        restClient.post()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        ProductResponseDto[] response = restClient.get()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(ProductResponseDto[].class);

        assertThat(response).isNotNull();
        ProductResponseDto product = response[response.length - 1];
        assertThat(product.name()).isEqualTo("녹차");
        assertThat(product.price()).isEqualTo(3500);
        assertThat(product.imageUrl()).isEqualTo("green_tea.jpg");
    }

    @Test
    void 상품을_수정하고_조회() {
        ProductRequestDto request = new ProductRequestDto("아이스 카페라떼", 7000, "ice_cafe_latte.jpg");

        restClient.put()
                .uri("/api/products/" + product.id())
                .header("Authorization", "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        ProductResponseDto response = restClient.get()
                .uri("/api/products/" + product.id())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(ProductResponseDto.class);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("아이스 카페라떼");
        assertThat(response.price()).isEqualTo(7000);
    }

    @Test
    void 상품을_삭제한다() {
        restClient.delete()
                .uri("/api/products/" + product.id())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.get()
                    .uri("/api/products/" + product.id())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 상품_등록_유효성_검사_실패() {
        ProductRequestDto invalidRequest = new ProductRequestDto("@카카오@", 10, "kakao.jpg");

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                    .uri("/api/products")
                    .header("Authorization", "Bearer " + token)
                    .body(invalidRequest)
                    .retrieve()
                    .toBodilessEntity();
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = exception.getResponseBodyAsString();

        assertThat(responseBody).contains("특수문자");
        assertThat(responseBody).contains("카카오");
        assertThat(responseBody).contains("100원");
    }

    private String 회원가입_후_토큰_발급() {
        String name = "홍길동";
        String email = "hong" + System.currentTimeMillis() + "@email.com";
        String password = "password";

        MemberRequestDto joinRequest = new MemberRequestDto(name, email, password);

        restClient.post()
                .uri("/api/members/register")
                .body(joinRequest)
                .retrieve()
                .toBodilessEntity();

        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(email, password);

        MemberLoginResponseDto loginResponse = restClient.post()
                .uri("/api/members/login")
                .body(loginRequest)
                .retrieve()
                .body(MemberLoginResponseDto.class);

        return loginResponse.token();
    }
}