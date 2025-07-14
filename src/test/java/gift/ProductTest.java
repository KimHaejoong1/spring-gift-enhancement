package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;
    private String baseUrl;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/products";
        restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Test
    public void 상품_추가_테스트() throws Exception {

        //given
        Product product = new Product();
        product.setName("상품 이름");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");

        //when
        var response = restClient.post()
                .body(ProductRequestDto.fromEntity(product))
                .retrieve()
                .toEntity(ApiResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto dto = objectMapper.convertValue(response.getBody().data(), ProductResponseDto.class);
        assertThat(dto.getName()).isEqualTo(product.getName());
        assertThat(dto.getPrice()).isEqualTo(product.getPrice());
        assertThat(dto.getImageUrl()).isEqualTo(product.getImageUrl());
    }

    @Test
    public void 비어있는_이름_상품_추가()  throws Exception {

        //given
        Product product = new Product();
        product.setName("");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");
        ProductRequestDto dto = ProductRequestDto.fromEntity(product);

        //when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        restClient.post()
                                .body(dto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .withMessageContaining("이름은 1~15자 여야합니다");

    }

    @Test
    public void 특수문자_이름_상품_추가()  throws Exception {

        //given
        Product product = new Product();
        product.setName("!!@#");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");
        ProductRequestDto dto = ProductRequestDto.fromEntity(product);

        //when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        restClient.post()
                                .body(dto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .withMessageContaining("특수문자는 (), [], +, -, &, /, _ 만 허용됩니다.");

    }

    @Test
    public void 카카오_이름_상품_추가() throws Exception {

        //given
        Product product = new Product();
        product.setName("카카오");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");
        ProductRequestDto dto = ProductRequestDto.fromEntity(product);

        //when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        restClient.post()
                                .body(dto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .withMessageContaining("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.");



    }

    @Test
    public void 빈_값_상품_추가()  throws Exception {

        //given
        Product product = new Product();
        product.setName("상품 이름");
        product.setPrice(null);
        product.setImageUrl("image.jpg");
        ProductRequestDto dto = ProductRequestDto.fromEntity(product);

        //when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        restClient.post()
                                .body(dto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .withMessageContaining("비울 수 없습니다");


    }



    @Test
    public void 전체_상품_조회()  throws Exception {


        //given
        Product product1 = new Product();
        product1.setName("상품 이름");
        product1.setPrice(10000);
        product1.setImageUrl("image.jpg");

        restClient.post().body(ProductRequestDto.fromEntity(product1)).retrieve().toEntity(ApiResponse.class);

        Product product2 = new Product();
        product2.setName("상품 이름2");
        product2.setPrice(10000);
        product2.setImageUrl("image2.jpg");

        restClient.post().body(ProductRequestDto.fromEntity(product2)).retrieve().toEntity(ApiResponse.class);

        //when
        var response = restClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ProductResponseDto> listDto = response.getBody();

        assertThat(listDto.get(6).getName()).isEqualTo(product1.getName());
        assertThat(listDto.get(6).getPrice()).isEqualTo(product1.getPrice());
        assertThat(listDto.get(6).getImageUrl()).isEqualTo(product1.getImageUrl());

        assertThat(listDto.get(7).getName()).isEqualTo(product2.getName());
        assertThat(listDto.get(7).getPrice()).isEqualTo(product2.getPrice());
        assertThat(listDto.get(7).getImageUrl()).isEqualTo(product2.getImageUrl());

    }

    @Test
    public void 상품_수정()  throws Exception {

        //given
        Product product = new Product();
        product.setId(5L);
        product.setName("상품 이름 수정");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");

        //when
        var response = restClient.put()
                .uri("/{id}" , product.getId())
                .body(ProductRequestDto.fromEntity(product))
                .retrieve()
                .toEntity(ApiResponse.class);

        //then

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto dto = objectMapper.convertValue(response.getBody().data(), ProductResponseDto.class);
        assertThat(dto.getId()).isEqualTo(product.getId());
        assertThat(dto.getName()).isEqualTo(product.getName());
        assertThat(dto.getPrice()).isEqualTo(product.getPrice());
        assertThat(dto.getImageUrl()).isEqualTo(product.getImageUrl());

    }

    public void 상품_삭제()  throws Exception {

        //given
        Product product = new Product();
        product.setId(5L);
        product.setName("상품 이름 수정");
        product.setPrice(10000);
        product.setImageUrl("image.jpg");

        //when
        var response = restClient.delete()
                .uri("/{id}" , product.getId())
                .retrieve()
                .toEntity(ApiResponse.class);


    }
}

