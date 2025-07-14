package gift.repository;

import gift.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void updateStatus(long id, Product.Status status);
    Long saveProduct(Product product);
    Optional<Product> findProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product);
    List<Product> findAllProducts();
}
