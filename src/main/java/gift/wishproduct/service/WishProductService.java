package gift.wishproduct.service;

import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.dto.WishProductResponse;
import gift.wishproduct.dto.WishProductUpdateReq;

import java.util.List;
import java.util.UUID;

public interface WishProductService {

    UUID save(WishProductCreateReq wishProductCreateReq, String email);

    List<WishProductResponse> findByEmail(String email);

    void deleteById(UUID id, String email);

    void updateQuantity(UUID id, WishProductUpdateReq wishProductUpdateReq, String email);
}
