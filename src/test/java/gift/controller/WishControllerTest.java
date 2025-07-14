package gift.controller;

import gift.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishControllerTest {
    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;
    private String baseUrl;

    @BeforeEach
    void setBaseUrl() {
        baseUrl = "http://localhost:"+ port;
    }

    @Test
    void Wish_추가_테스트() {
        String token = registerMemberAndGetToken("demo1@email", "asdf");

        ResponseEntity<String> responseEntity = addWish(token, 1L, 234);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void Wish_수정_테스트() {
        String token = registerMemberAndGetToken("demo2@email", "asdf");
        addWish(token, 1L, 123);

        ResponseEntity<String> responseEntity = updateWish(token, 1L, 321);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void Wish_GetList_테스트() {
        String token = registerMemberAndGetToken("demo3@email", "asdf");
        addWish(token, 1L, 123);

        ResponseEntity<WishesResponseDto> responseEntity = getWishList(token);

        assertAll(
                () -> assertThat(responseEntity.getBody().wishes()).isNotNull(),
                () -> assertThat(responseEntity.getBody().wishes().size()).isGreaterThan(0),
                () -> assertThat(responseEntity.getBody().wishes().getLast().count()).isEqualTo(123),
                () -> assertThat(responseEntity.getBody().wishes().getLast().productResponseDto().id()).isEqualTo(1)
        );
    }

    private String registerMemberAndGetToken(String email, String password) {
        ResponseEntity<AuthResponseDto> registerResponseEntity = restClient.post()
                .uri(baseUrl + "/api/members/register")
                .body(new MemberRequestDto(email, password))
                .retrieve()
                .toEntity(AuthResponseDto.class);

        return registerResponseEntity.getBody().token();
    }

    private ResponseEntity<String> addWish(String token, Long productId, int count) {
        return restClient.post()
                .uri(baseUrl + "/api/wishes/" + productId)
                .header("Authorization", "Bearer " + token)
                .body(new WishRequestDto(count))
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<String> updateWish(String token, Long productId, int count) {
        return restClient.patch()
                .uri(baseUrl + "/api/wishes/" + productId)
                .header("Authorization", "Bearer " + token)
                .body(new WishRequestDto(count))
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<WishesResponseDto> getWishList(String token) {
        return restClient.get()
                .uri(baseUrl + "/api/wishes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(WishesResponseDto.class);
    }
}
