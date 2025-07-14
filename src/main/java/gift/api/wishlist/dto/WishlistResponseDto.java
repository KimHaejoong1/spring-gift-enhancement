package gift.api.wishlist.dto;

import gift.api.product.domain.Product;
import gift.api.wishlist.domain.Wishlist;
import gift.api.product.dto.ProductResponseDto;

public record WishlistResponseDto(
        Long id,
        ProductResponseDto product
) {

    public static WishlistResponseDto of(Wishlist wishlist, Product product) {
        return new WishlistResponseDto(
                wishlist.getId(),
                ProductResponseDto.from(product)

        );
    }
}
