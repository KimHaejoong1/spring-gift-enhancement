package gift.repository;

import gift.entity.Wish;
import java.util.List;

public interface WishRepository {

    Wish save(Wish wish);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Wish> findByUserId(Long userId);
}