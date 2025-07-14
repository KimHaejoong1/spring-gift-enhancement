package gift.repository.product;


import gift.dto.product.ProductRequestDto;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Long create(Product product);

    Optional<Product> findById(Long productId);

    List<Product> findAllById(List<Long> idList);

    int update(Long productId, ProductRequestDto requestDto);

    void delete(Long productId);
}
