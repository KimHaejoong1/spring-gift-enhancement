package gift.product.controller;

import gift.member.annotation.MyAuthenticalPrincipal;
import gift.member.dto.AuthMember;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;
import gift.product.service.ProductService;
import gift.util.LocationGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductCreateRequest dto, @MyAuthenticalPrincipal AuthMember authMember) {

        UUID savedId = productService.save(dto, authMember.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).location(
                LocationGenerator.generate(savedId)
        ).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> response = productService.findAllProducts();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@MyAuthenticalPrincipal AuthMember authMember) {

        List<ProductResponse> response = productService.findByEmail(authMember);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {

        ProductResponse response = productService.findProduct(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id, @MyAuthenticalPrincipal AuthMember authMember) {
        productService.deleteProduct(id, authMember);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest dto,
                                              @MyAuthenticalPrincipal AuthMember authMember) {

        productService.updateProduct(id, dto, authMember);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
