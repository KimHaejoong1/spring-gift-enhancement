package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductStatusPatchRequestDto;
import gift.domain.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long saveProduct(ProductRequestDto productRequestDto) {
        return productRepository.saveProduct(
                productRequestDto.toEntity()
        );
    }

    public void deleteProductById(Long id) {
        productRepository.deleteProductById(id);
    }

    public void updateProduct(Long id, ProductRequestDto productUpdateDto) {
        productRepository.updateProduct(
                new Product(
                        id,
                        productUpdateDto.name(),
                        productUpdateDto.price(),
                        productUpdateDto.imageUrl())
        );
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findApprovedProducts() {
        return productRepository.findAllProducts().stream()
                .filter(Product::isApproved)
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAllProducts().stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        return productRepository.findProductById(id)
                .map(ProductResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("Not Found by id: " + id));
    }

    public void updateProductStatus(Long productId, ProductStatusPatchRequestDto statusPatchRequestDto) {
        productRepository.updateStatus(productId, statusPatchRequestDto.status());
    }
}
