package gift.wishlist.service;

import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultWishlistService implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public DefaultWishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public WishlistResponseDto addProductToWishlist(Long memberId, WishlistRequestDto requestDto) {
        return wishlistRepository.addProductToWishlist(memberId, requestDto);
    }

    @Override
    public List<WishlistResponseDto> getWishlist(Long memberId) {
        return wishlistRepository.getWishlist(memberId);
    }

    @Override
    public WishlistResponseDto deleteProductFromWishlist(Long memberId, Long productId) {
        return wishlistRepository.deleteProductFromWishlist(memberId, productId);
    }

    @Override
    public WishlistResponseDto updateProductQuantity(Long memberId, WishlistRequestDto requestDto) {
        return wishlistRepository.updateProductQuantity(memberId, requestDto);
    }
}
