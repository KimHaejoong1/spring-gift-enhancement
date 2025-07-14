package gift.wishlist.service;

import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.model.Wish;
import java.util.List;

public interface WishService {
    Wish addWish(Long memberId, WishRequestDto requestDto);
    List<WishResponseDto> getWishesByMemberId(Long memberId);
    void deleteWish(Long wishId, Long memberId);
}
