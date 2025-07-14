package gift.dto.wishlist;

public record WishListRequest(
    Long productId,
    int quantity
) {}
