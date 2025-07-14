package gift.product.service.impl;

import gift.product.dto.ProductRequestDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.exception.ProductValidationException;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(ProductRequestDto productDto) {
        validateProductName(productDto);

        Product product = productDto.toEntity();
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto productDto) {
        validateProductName(productDto);

        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        return productRepository.update(id, productDto);
    }

    @Override
    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }

    private void validateProductName(ProductRequestDto productDto) {
        try {
            productDto.validateProductName();
        } catch (IllegalArgumentException e) {
            throw new ProductValidationException(e.getMessage());
        }
    }
}
