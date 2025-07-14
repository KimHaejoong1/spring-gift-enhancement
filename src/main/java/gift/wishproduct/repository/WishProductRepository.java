package gift.wishproduct.repository;

import gift.domain.WishProduct;
import gift.wishproduct.dto.WishProductResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishProductRepository {

    WishProduct save(WishProduct wishProduct);

    Optional<WishProduct> findById(UUID id);

    List<WishProduct> findByOwnerId(UUID ownerId);

    List<WishProductResponse> findWithProductByOwnerId(UUID ownerId);

    List<WishProduct> findByProductId(UUID productId);


    Optional<WishProduct> findByOwnerIdAndProductId(UUID memberId, UUID productId);

    void deleteById(UUID id);

    void update(WishProduct wishProduct);

    void deleteAll();

}
