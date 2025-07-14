package gift.product.service;


import gift.domain.Product;
import gift.member.dto.AuthMember;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    UUID save(ProductCreateRequest dto, String email);
    List<ProductResponse> findAllProducts();
    ProductResponse findProduct(UUID id);
    void deleteProduct(UUID id, AuthMember authMember);
    void updateProduct(UUID id, ProductUpdateRequest dto, AuthMember authMember);
    List<ProductResponse> findByEmail(AuthMember authMember);
    Product findById(UUID id);
}
