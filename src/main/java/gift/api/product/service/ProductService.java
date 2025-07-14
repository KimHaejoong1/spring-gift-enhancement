package gift.api.product.service;

import gift.api.product.domain.Product;
import gift.api.product.dto.ProductRequestDto;
import gift.api.product.dto.ProductResponseDto;
import gift.api.product.repository.ProductRepository;
import gift.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductResponseDto> findAllProducts(Pageable pageable, Long categoryId) {
        Page<Product> page = productRepository.findAllProducts(pageable, categoryId);

        return page.map(ProductResponseDto::from);
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product createdProduct = productRepository.createProduct(
                new Product(
                        null,
                        productRequestDto.name(),
                        productRequestDto.price(),
                        productRequestDto.imageUrl()
                )
        );

        return ProductResponseDto.from(createdProduct);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        findProductById(id);

        Product updatedProduct = productRepository.updateProduct(
                new Product(
                        id,
                        productRequestDto.name(),
                        productRequestDto.price(),
                        productRequestDto.imageUrl()
                )
        );

        return ProductResponseDto.from(updatedProduct);
    }

    public void deleteProduct(Long id) {
        boolean deleted = productRepository.deleteProduct(id);

        if (!deleted) {
            throw new ProductNotFoundException(id);
        }
    }
}