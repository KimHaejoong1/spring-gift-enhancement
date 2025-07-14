package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private final RestClient client = RestClient.builder().build();

    @ParameterizedTest
    @MethodSource("invalidProducts")
    void Validation_테스트(ProductRequestDto productRequestDto, List<String> messages) {
        String url = "http://localhost:" + port + "/api/products";
        RestClient.ResponseSpec responseSpec = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve();

        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class,
                () -> {
                    responseSpec.toEntity(String.class);
                });

        boolean anyMatch = messages.stream()
                .anyMatch(exception.getMessage()::contains);

        assertThat(anyMatch).isTrue();
    }

    Stream<Arguments> invalidProducts() {
        return Stream.of(
                Arguments.of(new ProductRequestDto(
                        "1234567890123456",
                        123,
                        "http://~/~"
                ), List.of("length", "길이")),
                Arguments.of(new ProductRequestDto(
                        "$#",
                        123,
                        "http://~/~"
                ), List.of("special")),
                Arguments.of(new ProductRequestDto(
                        "1234567890",
                        -1,
                        "http://~/~"
                ), List.of("price"))
        );
    }

    @Test
    void 카카오_들어가는_이름_테스트_입력_테스트() {
        String url = "http://localhost:" + port + "/api/products";
        RestClient.ResponseSpec responseSpec = client.post()
                .uri(url)
                .body(new ProductRequestDto(
                        "카카오 들어감",
                        123,
                        "http://path/"
                ))
                .retrieve();

        ResponseEntity<String> response = responseSpec.toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String responseMessage = response.getBody();
        assertThat(responseMessage).isNotNull();

        Pattern pattern = Pattern.compile("id: (\\d+)");
        Matcher matcher = pattern.matcher(responseMessage);
        Long id = -1L;
        if (matcher.find()) {
            String idStr = matcher.group(1);
            id = Long.parseLong(idStr);
        }

        url = "http://localhost:" + port + "/api/products/" + id;
        RestClient.ResponseSpec getResponseSpec = client.get()
                .uri(url)
                .retrieve();
        ResponseEntity<ProductResponseDto> entity = getResponseSpec.toEntity(ProductResponseDto.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto productResponseDto = entity.getBody();
        assertThat(productResponseDto.status()).isEqualTo(Product.Status.PENDING);
    }

    @Test
    void 존재하는_상품_읽기_테스트() {
        String url = "http://localhost:" + port + "/api/products/1";
        RestClient.ResponseSpec response = client.get()
                .uri(url)
                .retrieve();
        ResponseEntity<ProductResponseDto> entity = response.toEntity(ProductResponseDto.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto productResponseDto = entity.getBody();
        assertThat(productResponseDto.name()).isEqualTo("상품 1");
    }

    @Test
    void 존재하지_않는_제폼_테스트() {
        String url = "http://localhost:" + port + "/api/products/3";
        RestClient.ResponseSpec response = client.get()
                .uri(url)
                .retrieve();

        HttpClientErrorException.BadRequest exception = assertThrows(HttpClientErrorException.BadRequest.class,
                () -> {
                    response.toEntity(String.class);
                });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
