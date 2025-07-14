package gift.controller;

import gift.dto.CreateMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberRequest;
import gift.dto.LoginMemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(statements = "delete from member")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberRestControllerTest {

    @LocalServerPort
    private int port;
    private RestClient client = RestClient.create();


    @Test
    @DisplayName("멤버 정상 등록")
    void 멤버_등록_정상() {
        String url = "http://localhost:" + port + "/api/members/register";
        ResponseEntity<CreateMemberResponse> response = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


    @Test
    @DisplayName("멤버 등록 에러 발생")
    void 멤버_등록_에러() {
        String url = "http://localhost:" + port + "/api/members/register";
        assertThatThrownBy(() ->
                client.post()
                        .uri(url)
                        .body(new CreateMemberRequest("test", "1234"))
                        .retrieve()
                        .toEntity(CreateMemberResponse.class)).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("멤버 정상 로그인")
    void 멤버_로그인_정상() {
        String url = "http://localhost:" + port + "/api/members/register";
        ResponseEntity<CreateMemberResponse> registerResponse = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        url = "http://localhost:" + port + "/api/members/login";
        ResponseEntity<LoginMemberResponse> loginResponse = client.post()
                .uri(url)
                .body(new LoginMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(LoginMemberResponse.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("멤버 로그인 에러(email 불일치)")
    void 멤버_로그인_에러_email불일치() {
        String url = "http://localhost:" + port + "/api/members/register";
        ResponseEntity<CreateMemberResponse> registerResponse = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        assertThatThrownBy(() ->
                client.post()
                        .uri("http://localhost:" + port + "/api/members/login")
                        .body(new LoginMemberRequest("test123@exam.com", "1234"))
                        .retrieve()
                        .toEntity(LoginMemberResponse.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    @DisplayName("멤버 로그인 에러(password 불일치)")
    void 멤버_로그인_에러_password불일치() {
        String url = "http://localhost:" + port + "/api/members/register";
        ResponseEntity<CreateMemberResponse> registerResponse = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);


        assertThatThrownBy(() ->
                client.post()
                        .uri("http://localhost:" + port + "/api/members/login")
                        .body(new LoginMemberRequest("test@exam.com", "12345"))
                        .retrieve()
                        .toEntity(LoginMemberResponse.class)
        ).isInstanceOf(HttpClientErrorException.Unauthorized.class);
    }
}