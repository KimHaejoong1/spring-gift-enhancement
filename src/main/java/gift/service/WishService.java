package gift.service;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import java.util.List;

public interface WishService {

    WishResponse addWish(Long memberId, WishRequest request);

    void deleteWish(Long memberId, Long productId);

    List<WishResponse> getWishes(Long memberId);
}