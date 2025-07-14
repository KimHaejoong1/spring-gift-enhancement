package gift;

import gift.member.dto.request.LoginRequestDto;
import gift.member.dto.response.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/test.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberAdminControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder builder;

    private RestClient client;

    @BeforeEach
    public void setUp() {
        this.client = builder.baseUrl("http://localhost:" + port + "/").build();
    }

    @Test
    void 관리자가_멤버_전체_조회_성공(){
        var loginDto = new LoginRequestDto("admin@daum.net", "123");

        var loginResponse = client.post()
                .uri("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginDto)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {});

        String token = loginResponse.get("token");

        var response = client.get()
                .uri("/api/admin/members")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<MemberResponseDto>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void 인증없이_멤버_전체_조회_실패() {
        var response = client.get()
                .uri("/api/admin/members")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Authorization ??? ????.\n");
    }
}
