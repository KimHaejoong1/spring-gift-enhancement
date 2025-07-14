package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.common.code.CustomResponseCode;
import gift.common.dto.CustomResponseBody;
import gift.dto.AuthRequest;
import gift.dto.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client;

    private final String TEST_EMAIL = "testuser1@domain.com";
    private final String TEST_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port + "/api/auth";
        client = RestClient.builder().baseUrl(baseUrl).build();

        AuthRequest request = new AuthRequest(TEST_EMAIL, TEST_PASSWORD);
        client.post()
            .uri("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toBodilessEntity();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testRegisterSuccess() {
        AuthRequest request = new AuthRequest("testuser2@domain.com", "password");

        CustomResponseBody<AuthResponse> response = client.post()
            .uri("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(CustomResponseCode.CREATED.getCode());
        assertThat(response.data()).isNull();
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest(TEST_EMAIL, TEST_PASSWORD);

        CustomResponseBody<AuthResponse> loginResponse = client.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.status()).isEqualTo(CustomResponseCode.LOGIN_SUCCESS.getCode());
        assertThat(loginResponse.data()).isNotNull();
        assertThat(loginResponse.data().token()).isNotBlank();
    }

    @Test
    @DisplayName("이메일 형식 유효성 실패")
    void testEmailFormatValidation() {
        AuthRequest request = new AuthRequest("email", "password");

        ResponseEntity<String> response = client.post()
            .uri("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "올바른 이메일은 형식이 아닙니다.");
    }

    @Test
    @DisplayName("비밀번호 누락 유효성 실패")
    void testPasswordBlankValidation() {
        AuthRequest request = new AuthRequest("영진@email.com", "");

        ResponseEntity<String> response = client.post()
            .uri("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            })
            .toEntity(String.class);

        assertValidationError(response, "비밀번호는 필수입니다.");
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
        assertAll("유효성 오류 응답 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.getStatusCode().value())
                .isEqualTo(CustomResponseCode.VALIDATION_FAILED.getHttpStatus().value()),
            () -> assertThat(response.getBody()).contains(expectedMessage)
        );
    }
}
