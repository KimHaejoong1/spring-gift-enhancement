package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.IdResponse;
import gift.dto.jwt.TokenResponse;
import gift.dto.member.MemberRequest;
import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.global.exception.ErrorCode;
import gift.global.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("일반 사용자용 Product API 테스트")
public class ProductApiTest {

    private final RestClient restClient = RestClient.builder().build();

    private String jwtToken="";

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void loginSetTup(){
        MemberRequest memberRequest = new MemberRequest(null, "member1@mem", "password1");
        var loginUrl = "http://localhost:" + port + "/api/members/login";

        // 로그인 API 호출하여 JWT 토큰 받기
        var loginResponse = restClient.post()
            .uri(loginUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberRequest)
            .retrieve()
            .toEntity(TokenResponse.class);

        // 로그인 성공 확인 및 토큰 추출
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        this.jwtToken = loginResponse.getBody().token(); // JWT 토큰 저장
        assertThat(jwtToken).isNotBlank(); // 토큰이 비어있지 않은지 확인
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM products");
        jdbcTemplate.update("ALTER TABLE products ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p1",
            1000, "url1");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p2",
            2000, "url2");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p3",
            3000, "url3");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p4",
            4000, "url4");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p5",
            5000, "url5");
    }

    @Test
    @DisplayName("상품 단건 조회")
    void 상품_조회() {
        var url = "http://localhost:" + port + "/api/products/1";

        var response = restClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(ProductResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("p1");
    }

    @Test
    @DisplayName("상품 전체 조회")
    void 상품_전체_조회() {
        var url = "http://localhost:" + port + "/api/products/all";

        var response = restClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            // List로 변환하지 않고 배열로 간단하게 처리
            .toEntity(ProductResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(5);
    }

    @Test
    @DisplayName("상품 등록")
    void 상품_등록() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequest request = new ProductRequest(null, "test1", 100, "test url");

        var response = restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toEntity(IdResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    @DisplayName("상품 수정")
    void 상품_수정() {
        Long productId = 5L;
        var url = "http://localhost:" + port + "/api/products";
        ProductRequest updateRequest = new ProductRequest(productId, "updated name", 2000,
            "updated url");

        var response = restClient.patch()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateRequest)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var url2 = "http://localhost:" + port + "/api/products/"+productId;
        var getResponse = restClient.get()
            .uri(url2)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(ProductResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().name()).isEqualTo("updated name");
        assertThat(getResponse.getBody().price()).isEqualTo(2000);
    }


    @Test
    @DisplayName("상품 삭제")
    void 상품_삭제() {
        Long productId = 5L;
        var url = "http://localhost:" + port + "/api/products/" + productId;

        var response = restClient.delete()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var notFoundResponse = restClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res) -> {
            })
            .toBodilessEntity();

        assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("상품 단건 조회 예외발생")
    void 상품_단건_조회_예외발생(){
        Long productId = 999L;
        var url = "http://localhost:" + port + "/api/products/" + productId;

        var response = restClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(ErrorCode.NOT_EXISTS);
        assertThat(response.getBody().message()).isEqualTo(ErrorCode.NOT_EXISTS.getErrorMessage());
    }

    @Test
    @DisplayName("상품 등록 예외 발생")
    void 상품_등록_예외_발생(){
        var url = "http://localhost:" + port + "/api/products";

        // name 필드에 허용되지 않는 특수문자
        ProductRequest request = new ProductRequest(null, "test%", 100, "test url");
        var response1 = restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse[].class);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getBody()[0].errorCode()).isEqualTo(ErrorCode.INVALID_FORM_REQUEST);
        assertThat(response1.getBody()[0].message()).isEqualTo("사용가능한 특수문자: (), [], +, -, &, /, _");

        // name 필드 15자 초과
        request = new ProductRequest(null, "testtesttesttest", 100, "test url");
        var response2 = restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse[].class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody()[0].errorCode()).isEqualTo(ErrorCode.INVALID_FORM_REQUEST);
        assertThat(response2.getBody()[0].message()).isEqualTo("상품명은 최대 15자까지 가능합니다.");

        // name 필드에 '카카오' 이름 포함
        request = new ProductRequest(null, "test카카오", 100, "test url");
        var response3 = restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response3.getBody()).isNotNull();
        assertThat(response3.getBody().errorCode()).isEqualTo(ErrorCode.INVALID_KAKAO_NAME);
        assertThat(response3.getBody().message()).isEqualTo(ErrorCode.INVALID_KAKAO_NAME.getErrorMessage());

    }

    @Test
    @DisplayName("상품 수정 예외 발생")
    void 상품_수정_예외_발생(){

        // 유효하지 않은 name 필드 제공
        Long productId = 5L;
        var url = "http://localhost:" + port + "/api/products";
        ProductRequest updateRequest = new ProductRequest(productId, "updated name%", 2000,
            "updated url");

        var response1 = restClient.patch()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateRequest)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse[].class);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getBody()[0].errorCode()).isEqualTo(ErrorCode.INVALID_FORM_REQUEST);
        assertThat(response1.getBody()[0].message()).isEqualTo("사용가능한 특수문자: (), [], +, -, &, /, _");


        // 존재하지 않는 id
        productId = 999L;
        url = "http://localhost:" + port + "/api/products";
        updateRequest = new ProductRequest(productId, "updated name", 2000,
            "updated url");

        var response2 = restClient.patch()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateRequest)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().errorCode()).isEqualTo(ErrorCode.NOT_EXISTS);
        assertThat(response2.getBody().message()).isEqualTo(ErrorCode.NOT_EXISTS.getErrorMessage());

    }

    @Test
    @DisplayName("상품 삭제 예외 발생")
    void 상품_삭제_예외_발생(){
        Long productId = 999L;
        var url = "http://localhost:" + port + "/api/products/" + productId;

        var response = restClient.delete()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(ErrorCode.NOT_EXISTS);
        assertThat(response.getBody().message()).isEqualTo(ErrorCode.NOT_EXISTS.getErrorMessage());
    }
}
