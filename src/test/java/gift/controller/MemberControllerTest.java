package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;

    @Test
    void 회원가입_로그인_테스트() {
        ResponseEntity<AuthResponseDto> registerResponseEntity = restClient.post()
                .uri("http://localhost:"+ port + "/api/members/register")
                .body(new MemberRequestDto(
                        "demo@demo",
                        "demopass"
                ))
                .retrieve()
                .toEntity(AuthResponseDto.class);

        assertThat(registerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<AuthResponseDto> loginResponseEntity = restClient.post()
                .uri("http://localhost:"+ port + "/api/members/login")
                .body(new MemberRequestDto(
                        "demo@demo",
                        "demopass"
                ))
                .retrieve()
                .toEntity(AuthResponseDto.class);

        assertThat(loginResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

         RestClient.ResponseSpec responseSpec = restClient.post()
                .uri("http://localhost:"+ port + "/api/members/login")
                .body(new MemberRequestDto(
                        "demo@demo",
                        "demopasss"
                ))
                .retrieve();
         HttpClientErrorException.BadRequest exception = Assertions.assertThrows(
                 HttpClientErrorException.BadRequest.class,
                 () -> responseSpec.toEntity(AuthResponseDto.class));

        assertThat(exception.getResponseBodyAsString()).contains("비밀번호");
    }

    @Test
    void 회원가입_중복_이메일_테스트() {
        ResponseEntity<AuthResponseDto> registerResponseEntity = restClient.post()
                .uri("http://localhost:"+ port + "/api/members/register")
                .body(new MemberRequestDto(
                        "demo@email",
                        "demopass"
                ))
                .retrieve()
                .toEntity(AuthResponseDto.class);

        assertThat(registerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        RestClient.ResponseSpec responseSpec = restClient.post()
                .uri("http://localhost:"+ port + "/api/members/register")
                .body(new MemberRequestDto(
                        "demo@email",
                        "demopass"
                ))
                .retrieve();
        HttpClientErrorException.BadRequest exception = Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> responseSpec.toEntity(AuthResponseDto.class));

        assertThat(exception.getResponseBodyAsString()).contains("email");
    }
}
