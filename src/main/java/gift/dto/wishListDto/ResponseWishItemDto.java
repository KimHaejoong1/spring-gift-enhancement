package gift.dto.wishListDto;

import gift.entity.WishItem;

public record ResponseWishItemDto(Long id,Long userId, Long itemId, Integer quantity) {
    public static ResponseWishItemDto from(WishItem wishItem) {
        return new ResponseWishItemDto(wishItem.id(), wishItem.userId(), wishItem.itemId(), wishItem.quantity());
    }

    public static ResponseWishItemDto delete(WishItem wishItem) {
        return new ResponseWishItemDto(wishItem.id(), wishItem.userId(), wishItem.itemId(), 0);
    }

}
