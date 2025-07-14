package gift.wishlist.dto;

public record WishlistResponseDto(
        Long productId,
        String productName,
        int quantity,
        int price,
        String imageUrl
) { }
