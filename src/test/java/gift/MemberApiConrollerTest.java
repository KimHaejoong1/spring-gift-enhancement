package gift;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberApiConrollerTest {

    @LocalServerPort
    private int port;

    String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/members";

        memberRequestDto = new MemberRequestDto(
                "test@email.com",
                "testpassword",
                Role.USER
        );
    }

    @Test
    void 회원_가입_테스트() {

        //given
        HttpEntity<MemberRequestDto> request = new HttpEntity<>(memberRequestDto);

        //when
        ResponseEntity<TokenResponseDto> response = restTemplate.postForEntity(
                baseUrl + "/register",
                request,
                TokenResponseDto.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        TokenResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.token()).isNotBlank();
    }

    @Test
    void 로그인_테스트() {

        MemberRequestDto signUpDto = new MemberRequestDto(
                "loginuser@email.com",
                "loginpassword",
                Role.USER
        );
        restTemplate.postForEntity(baseUrl + "/register", signUpDto, TokenResponseDto.class);

        MemberRequestDto loginRequest = new MemberRequestDto(
                "loginuser@email.com",
                "loginpassword",
                Role.USER
        );

        // 로그인 요청
        ResponseEntity<TokenResponseDto> response = restTemplate.postForEntity(
                baseUrl + "/login",
                loginRequest,
                TokenResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TokenResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.token()).isNotBlank();
    }
}


