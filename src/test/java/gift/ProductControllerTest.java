package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "/api/products";

    private RestClient client;

    @BeforeEach
    void setUp() {
        this.client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("상품 생성 - 성공")
    void createProduct_success() {
        var requestDto = new ProductRequestDto("test product", 1000, "www.example.com");

        var response = client.post()
            .uri(BASE_URL)
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 생성 - 실패 ('카카오' 포함 시 유효성 검사 400 Bad Request)")
    void createProduct_fail_whenNameContainsKakao() {
        var requestDto = new ProductRequestDto("카카오", 0, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 생성 - 실패 (이름이 빈칸일 경우 400 Bad Request)")
    void createProduct_fail_whenNameIsBlank() {
        var requestDto = new ProductRequestDto("", 0, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 생성 - 실패 (이름 15자 초과 시 400 Bad Request)")
    void createProduct_fail_whenNameExceedsMaxLength() {
        var requestDto = new ProductRequestDto("ddddddddddddddd", 0, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 생성 - 실패 (이름에 허용되지 않은 특수문자 포함 시 400 Bad Request)")
    void createProduct_fail_whenNameContainsInvalidSpecialChars() {
        var requestDto = new ProductRequestDto("###", 0, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 생성 - 실패 (가격이 음수일 경우 400 Bad Request)")
    void createProduct_fail_whenPriceIsNegative() {
        var requestDto = new ProductRequestDto("test product", -1, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 생성 - 실패 (이미지 URL이 빈칸일 경우 400 Bad Request)")
    void createProduct_fail_whenImageUrlIsBlank() {
        var requestDto = new ProductRequestDto("test product", 1000, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.post()
                        .uri(BASE_URL)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 단건 조회 - 성공")
    void findProductById_success() {
        var response = client.get()
            .uri(BASE_URL + "/1")
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 단건 조회 - 실패 (존재하지 않는 상품 조회 시 404 Not Found 응답)")
    void findProductById_fail_notFound() {
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
            .isThrownBy(
                () ->
                    client.get()
                        .uri(BASE_URL + "/4")
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 목록 전체 조회 - 성공")
    void findAllProducts_success() {
        var response = client.get()
            .uri(BASE_URL)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {
            });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("상품 수정 - 성공")
    void updateProduct_success() {
        var requestDto = new ProductRequestDto("test product", 1000, "www.example.com");

        var response = client.put()
            .uri(BASE_URL + "/2")
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("상품 수정 - 실패 (존재하지 않는 상품 ID로 요청 시 404 Not Found 응답)")
    void updateProduct_fail_whenProductNotFound() {
        var requestDto = new ProductRequestDto("test", 0, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/6")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (이름이 '카카오'를 포함)")
    void updateProduct_fail_whenNameContainsKakao() {
        var requestDto = new ProductRequestDto("카카오", 0, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (이름이 빈칸)")
    void updateProduct_fail_whenNameIsBlank() {
        var requestDto = new ProductRequestDto("", 0, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (이름이 15자 초과)")
    void updateProduct_fail_whenNameExceedsMaxLength() {
        var requestDto = new ProductRequestDto("ddddddddddddddd", 0, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (이름에 허용되지 않은 특수문자 포함)")
    void updateProduct_fail_whenNameContainsInvalidSpecialChars() {
        var requestDto = new ProductRequestDto("###", 0, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (가격이 음수)")
    void updateProduct_fail_whenPriceIsNegative() {
        var requestDto = new ProductRequestDto("test product", -1, "www.example.com");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 수정 - 실패 (이미지 URL이 빈칸)")
    void updateProduct_fail_whenImageUrlIsBlank() {
        var requestDto = new ProductRequestDto("test product", 1000, "");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () ->
                    client.put()
                        .uri(BASE_URL + "/1")
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Void.class)
            );
    }

    @Test
    @DisplayName("상품 삭제 - 성공")
    void deleteProduct_success() {
        var response = client.delete()
            .uri(BASE_URL + "/1")
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("상품 삭제 - 실패 (존재하지 않는 상품 ID로 삭제 시 404 Not Found 응답)")
    void deleteProduct_fail_notFound() {
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
            .isThrownBy(() ->
                client.delete()
                    .uri(BASE_URL + "/5")
                    .retrieve()
                    .toBodilessEntity()
            );
    }
}