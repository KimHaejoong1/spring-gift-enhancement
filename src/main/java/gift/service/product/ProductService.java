package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto findById(Long id);

    ProductResponseDto update(Long id, ProductRequestDto requestDto);

    void delete(Long id);
}
