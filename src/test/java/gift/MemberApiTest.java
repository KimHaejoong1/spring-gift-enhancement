package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.jwt.TokenResponse;
import gift.dto.member.MemberRequest;
import gift.dto.IdResponse;
import gift.global.exception.ErrorCode;
import gift.global.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("member API 테스트")
public class MemberApiTest {

    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM members");
        jdbcTemplate.update("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update("INSERT INTO members(email, password) VALUES('member1@mem', '$2a$10$141vsuswYHZmSMgqvTUP/u2sfY1881BOE32M7n8UdlHG/2XZ7IEuO')");
        jdbcTemplate.update("INSERT INTO members(email, password) VALUES('member2@mem', '$2a$10$.2HSQpYl0kRbMqUrnb7V9eGJOimko1SIP0w.1TCN37LW0VNxbdzLq')");
        jdbcTemplate.update("INSERT INTO members(email, password) VALUES('member3@mem', '$2a$10$Sy9sN2w9D66Tsdm3flXjQeEmb/6QhIZlSiKykUMZ9lLJrcgG/IdJ.')");
        jdbcTemplate.update("INSERT INTO members(email, password) VALUES('member4@mem', '$2a$10$Xp52WG3ZkvthBuYk3r0Q6.58UAyX5qCoFymPG4.UYl6kQpxWSHWr2')");
        jdbcTemplate.update("INSERT INTO members(email, password) VALUES('member5@mem', '$2a$10$cVqSwvAyY2P1RlJMyj3ZJeqcMHzpJPvky8PLf0n1qjyy9zzu6FLk.')");
    }

    @Test
    void 회원가입하면_201_반환() {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequest memberRequest = new MemberRequest(null, "test@test", "test");

        var response = restClient.post()
            .uri(url)
            .body(memberRequest)
            .retrieve()
            .toEntity(IdResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 중복_이메일로_회원가입하면_403(){
        // dummy 계정 생성
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequest memberRequest = new MemberRequest(null, "test1@test1", "test1");

        var response = restClient.post()
            .uri(url)
            .body(memberRequest)
            .retrieve()
            .toEntity(IdResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Long dummyId = response.getBody().id();

        // 중복 요청
        String url2 = "http://localhost:" + port + "/api/members/register";
        MemberRequest memberRequest2 = new MemberRequest(null, "test1@test1", "test1");

        var response2 = restClient.post()
            .uri(url2)
            .body(memberRequest2)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response2.getBody().message()).isEqualTo(ErrorCode.DUPLICATE_EMAIL.getErrorMessage());
    }

    @Test
    void 로그인하면_200_반환() {
        String url = "http://localhost:" + port + "/api/members/login";
        MemberRequest memberRequest = new MemberRequest(null, "member1@mem", "password1");

        var response = restClient.post()
            .uri(url)
            .body(memberRequest)
            .retrieve()
            .toEntity(TokenResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 로그인_이메일_비밀번호_잘못되면_403(){
        String url = "http://localhost:" + port + "/api/members/login";
        MemberRequest MemberRequest = new MemberRequest(null, "member1@mem", "password2");

        var response = restClient.post()
            .uri(url)
            .body(MemberRequest)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res)->{})
            .toEntity(ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().message()).isEqualTo(ErrorCode.INCORRECT_LOGIN_INFO.getErrorMessage());
    }
}
