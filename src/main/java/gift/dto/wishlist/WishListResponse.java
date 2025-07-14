package gift.dto.wishlist;


public record WishListResponse(
   Long memberId,
   String memberEmail,

   Long productId,
   String productName,
   int productPrice,
   int quantity,
   int totalPrice
) {

}
