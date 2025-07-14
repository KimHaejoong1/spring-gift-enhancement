package gift.repository;

import gift.domain.Wish;

import java.util.List;
import java.util.Optional;

public interface WishRepository {

    Wish addWish(Long memberId, Long productId);

    boolean existsInWishlist(Long memberId, Long productId);

    List<Wish> getWishlistByMemberId(Long memberId);

    Optional<Wish> findById(Long id);

    void removeByMemberIdAndWishId(Long memberId, Long wishId);

}
