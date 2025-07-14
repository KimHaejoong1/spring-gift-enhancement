package gift.service.wishlist;

import gift.dto.product.ProductResponseDto;
import gift.entity.Wish;
import java.util.List;

public interface WishListService {
    Wish create(Long productId, Long memberId);

    List<ProductResponseDto> findAll(Long memberId);

    void delete(Long productId, Long memberId);
}