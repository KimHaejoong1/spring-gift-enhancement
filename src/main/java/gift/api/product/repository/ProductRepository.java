package gift.api.product.repository;

import gift.api.product.domain.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Page<Product> findAllProducts(Pageable pageable, Long categoryId);

    Optional<Product> findProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteProduct(Long id);
}