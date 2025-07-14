package gift.product.repository;

import gift.product.dto.ProductRequestDto;
import gift.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product update(Long id, ProductRequestDto productRequestDto);
    void deleteById(Long id);
}
