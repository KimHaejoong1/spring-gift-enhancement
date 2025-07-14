package gift.dto;

public record CreateWishResponse(Long id, Long memberId, Long productId, int quantity) {
}
