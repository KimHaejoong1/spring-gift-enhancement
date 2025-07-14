package gift.wishlist.service;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;

import java.util.List;

public interface WishlistService {

    WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto);
    List<WishlistResponseDto> getWishlist(Long memberId);
    WishlistResponseDto deleteProductFromWishlist(Long memberId, Long productId);
    WishlistResponseDto updateProductQuantity(Long memberId, WishlistRequestDto requestDto);
}
