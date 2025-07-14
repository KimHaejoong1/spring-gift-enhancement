package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.dto.member.MemberPasswordChangeDto;
import gift.dto.member.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private final static String BASE_URL = "/api/members";

    private RestClient client;

    @BeforeEach
    void Setup() {
        this.client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("회원 등록 - 성공")
    void createMember_success() {
        var requestDto = new MemberRequestDto("example1@naver.com", "qwer");

        var response = client.post()
            .uri(BASE_URL + "/register")
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("회원 등록 - 실패")
    void createMember_fail() {
        var requestDto = new MemberRequestDto("example@naver.com", "qwer");

        assertThatExceptionOfType(HttpClientErrorException.Conflict.class).isThrownBy(() ->
            client.post()
                .uri(BASE_URL + "/register")
                .body(requestDto)
                .retrieve()
                .toBodilessEntity()
        );
    }

    @Test
    @DisplayName("회원 로그인 - 성공")
    void loginMember_success() {
        var requestDto = new MemberRequestDto("example@naver.com", "qwer");

        var response = client.post()
            .uri(BASE_URL + "/login")
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원 로그인 - 실패")
    void loginMember_fail() {
        var requestDto = new MemberRequestDto("example1@naver.com", "qwer1");

        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
            .isThrownBy(() ->
                client.post()
                    .uri(BASE_URL + "/login")
                    .body(requestDto)
                    .retrieve()
                    .toBodilessEntity()
            );
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void changePassword_success() {
        var requestDto = new MemberPasswordChangeDto("example1@naver.com", "qwer", "qwer1234");

        var response = client.put()
            .uri(BASE_URL + "/password/change")
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("비밀번호 변경 - 실패1 (아이디만 틀린 경우)")
    void changePassword_fail1() {
        var requestDto = new MemberPasswordChangeDto("test@naver.com", "qwer", "qwer1234");

        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
            .isThrownBy(() ->
                client.put()
                    .uri(BASE_URL + "/password/change")
                    .body(requestDto)
                    .retrieve()
                    .toBodilessEntity()
            );
    }

    @Test
    @DisplayName("비밀번호 변경 - 실패2 (비밀번호만 틀린 경우)")
    void changePassword_fail2() {
        var requestDto = new MemberPasswordChangeDto("example@naver.com", "qwer1234", "qwer");

        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
            .isThrownBy(() ->
                client.put()
                    .uri(BASE_URL + "/password/change")
                    .body(requestDto)
                    .retrieve()
                    .toBodilessEntity()
            );
    }

    @Test
    @DisplayName("비밀번호 재설정 - 성공")
    void resetPassword_success() {
        var requestDto = new MemberRequestDto("example@naver.com", "qwer");

        var response = client.post()
            .uri(BASE_URL + "/password/reset")
            .body(requestDto)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("비밀번호 재설정 - 실패 (아이디가 틀린 경우)")
    void resetPassword_fail() {
        var requestDto = new MemberRequestDto("test@naver.com", "qwer12");

        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
            .isThrownBy(() ->
                client.post()
                    .uri(BASE_URL + "/password/reset")
                    .body(requestDto)
                    .retrieve()
                    .toBodilessEntity()
            );
    }
}