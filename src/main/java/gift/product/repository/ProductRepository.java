package gift.product.repository;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    ProductResponseDto saveProduct(ProductRequestDto productRequestDto);
    Optional<ProductResponseDto> findProductById(Long id);
    List<ProductResponseDto> findAllProducts();
    void updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);

}
