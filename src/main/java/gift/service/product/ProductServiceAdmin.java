package gift.service.product;

import gift.domain.Product;
import gift.dto.product.ProductRequest;
import gift.repository.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceAdmin {

    private final ProductRepository productRepository;

    public ProductServiceAdmin(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductByIdAdmin(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getProductListAdmin() {
        return productRepository.findAll();
    }

    public void insertAdmin(Product product) {
        productRepository.insert(product);
    }

    public void updateAdmin(ProductRequest request) {
        productRepository.findById(request.id());

        productRepository.update(Product.from(request));
    }

    public void deleteByIdAdmin(Long productId) {
        productRepository.findById(productId);

        productRepository.deleteById(productId);
    }

}
