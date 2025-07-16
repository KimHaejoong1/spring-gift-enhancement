package gift.repository;

import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_success() {
        Product product = new Product("휠렛버거", BigInteger.valueOf(5000), "https://example.com/image.jpg");

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("휠렛버거");
        assertThat(saved.getPrice()).isEqualTo(BigInteger.valueOf(5000));
        assertThat(saved.getImageUrl()).isEqualTo("https://example.com/image.jpg");
    }

    @Test
    void findById_success() {
        Product product = new Product("휠렛버거", BigInteger.valueOf(5000), "https://example.com/image.jpg");
        Product saved = entityManager.persistAndFlush(product);

        Optional<Product> found = productRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("휠렛버거");
        assertThat(found.get().getPrice()).isEqualTo(BigInteger.valueOf(5000));
    }
    @Test
    void findById_not_found() {
        Optional<Product> found = productRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_success() {
        Product product1 = new Product("상품1", BigInteger.valueOf(1000), "https://example.com/1.jpg");
        Product product2 = new Product("상품2", BigInteger.valueOf(2000), "https://example.com/2.jpg");
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting("name").containsExactlyInAnyOrder("상품1", "상품2");
    }

    @Test
    void deleteById_success() {
        Product product = new Product("휠렛버거", BigInteger.valueOf(5000), "https://example.com/image.jpg");
        Product saved = entityManager.persistAndFlush(product);

        productRepository.deleteById(saved.getId());

        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void existsById_success() {
        Product product = new Product("휠렛버거", BigInteger.valueOf(5000), "https://example.com/image.jpg");
        Product saved = entityManager.persistAndFlush(product);

        assertThat(productRepository.existsById(saved.getId())).isTrue();
        assertThat(productRepository.existsById(999)).isFalse();
    }
}
