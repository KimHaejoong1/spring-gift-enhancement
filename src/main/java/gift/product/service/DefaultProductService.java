package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.common.exception.ForbiddenWordException;
import gift.common.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 상품 생성
    @Override
    public ProductResponseDto addProduct(ProductRequestDto requestDto) {
        if (requestDto.name().contains("카카오")) {
            throw new ForbiddenWordException("카카오");
        }

        return productRepository.saveProduct(requestDto);
    }

    // 특정 상품 조회
    @Override
    public ProductResponseDto getProductById(Long id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // 모든 상품 조회
    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllProducts();
    }

    // 특정 상품 수정
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        productRepository.updateProduct(id, requestDto);
        return getProductById(id);
    }

    // 특정 상품 삭제
    @Override
    public ProductResponseDto deleteProduct(Long id) {
        ProductResponseDto product = getProductById(id);
        productRepository.deleteProduct(id);
        return product;
    }

    private RowMapper<Product> productRowMapper() {
        return (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    }
}
