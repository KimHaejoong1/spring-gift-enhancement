package gift.dto;

import gift.entity.Product;
import gift.entity.Wish;

public record WishResponse(
    Long wishId,
    Long productId,
    Integer quantity,
    String productName,
    Integer price,
    String imageUrl
) {

    public static WishResponse from(Wish wish, Product product) {
        return new WishResponse(
            wish.getId(),
            product.getId(),
            wish.getQuantity(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
