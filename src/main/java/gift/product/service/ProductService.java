package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto addProduct(ProductRequestDto requestDto);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

    ProductResponseDto deleteProduct(Long id);

    List<ProductResponseDto> getAllProducts();
}
