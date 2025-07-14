package gift.wishlist.repository;

import gift.wishlist.model.Wish;
import java.util.List;
import java.util.Optional;

public interface WishRepository {
    Wish save(Wish wish);
    List<Wish> findByMemberId(Long memberId);
    Optional<Wish> findByProductId(Long productId);
    void deleteById(Long id);
    boolean exists(Long memberId, Long productId);
}
