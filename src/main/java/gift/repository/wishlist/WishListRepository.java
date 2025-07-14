package gift.repository.wishlist;

import gift.entity.Wish;
import java.util.List;

public interface WishListRepository {
    public Wish create(Wish wish);

    List<Wish> findAll(Long memberId);

    int delete(Long productId, Long memberId);
}