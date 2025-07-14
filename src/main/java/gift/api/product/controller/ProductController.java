package gift.api.product.controller;

import gift.api.product.dto.ProductRequestDto;
import gift.api.product.dto.ProductResponseDto;
import gift.api.product.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(productService.findAllProducts(pageable, categoryId).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);

        URI location = URI.create("/admin/products/" + createdProduct.id());

        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
