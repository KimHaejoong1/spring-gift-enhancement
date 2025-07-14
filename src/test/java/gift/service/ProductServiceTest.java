package gift.service;

import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품이_정상적으로_등록된다() {
        // given
        String name = "초코파이";
        int price = 1000;
        String imageUrl = "http://example.com/chocopie.jpg";

        // when
        Product result = productService.create(name, price, imageUrl);

        // then
        assertNotNull(result);
        assertEquals("초코파이", result.getName());
    }

    @Test
    void 상품명에_카카오가_포함되면_예외가_발생한다() {
        // given
        String name = "카카오";
        int price = 2000;
        String imageUrl = "http://example.com/kakao.jpg";

        // when & then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(name, price, imageUrl)
        );

        assertEquals("'카카오'가 포함된 상품명은 MD와 협의 후 등록 가능합니다.", e.getMessage());
    }

    @Test
    void 존재하지_않는_ID로_수정하면_예외가_발생한다() {
        // given
        Long invalidId = 999L;
        String name = "새로운 이름";
        int price = 3000;
        String imageUrl = "http://example.com/image.jpg";

        // when & then
        assertThrows(ProductNotFoundException.class, () ->
                productService.update(invalidId, name, price, imageUrl));
    }

    @Test
    void 상품명에_카카오가_포함되면_업데이트에서도_예외가_발생한다() {
        // given
        Product saved = productService.create("초코송이", 1000, "http://img.jpg");

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                productService.update(saved.getId(), "카카오 초코송이", 1000, "http://img.jpg"));
    }

    @Test
    void 수정사항이_없어도_예외없이_정상처리된다() {
        // given
        Product saved = productService.create("몽쉘", 1500, "http://img.jpg");

        // when & then
        assertDoesNotThrow(() ->
                productService.update(saved.getId(), "몽쉘", 1500, "http://img.jpg"));
    }

    @Test
    void 상품이_정상적으로_수정된다() {
        // given
        Product savedProduct = productService.create("새우깡", 1200, "http://img.jpg");

        // when
        assertDoesNotThrow(() ->
                productService.update(savedProduct.getId(), "매운 새우깡", 1300, "http://img2.jpg"));

        // then
        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals("매운 새우깡", updatedProduct.getName());
        assertEquals(1300, updatedProduct.getPrice());
        assertEquals("http://img2.jpg", updatedProduct.getImageUrl());
    }

    @Test
    void 존재하지_않는_ID로_삭제해도_예외가_발생하지_않는다() {
        // given
        Long invalidId = 999L;

        // when & then
        assertDoesNotThrow(() -> productService.delete(invalidId));
    }

}
