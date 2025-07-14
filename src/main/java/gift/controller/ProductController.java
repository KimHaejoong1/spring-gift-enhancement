package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductStatusPatchRequestDto;
import gift.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<String> createProduct(
            @Validated @RequestBody ProductRequestDto productRequestDto) {
        Long productId = productService.saveProduct(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created id: " + productId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @Validated @RequestBody ProductRequestDto productRequestDto
    ) {
        productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getApprovedProducts() {
        return ResponseEntity.ok(productService.findApprovedProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }
    
    @PatchMapping("/{productId}/status")
    public ResponseEntity<String> patchProductStatus(
            @PathVariable Long productId,
            @RequestBody ProductStatusPatchRequestDto statusPatchRequestDto
            ) {
        productService.updateProductStatus(productId, statusPatchRequestDto);
        return ResponseEntity.ok("Successfully Update Product's Status");
    }
}
