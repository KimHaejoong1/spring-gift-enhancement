package gift.repository.wishListRepository;

import gift.entity.WishItem;

import java.util.List;

public interface WishListRepository {

    WishItem addWishItem(Long userId, Long itemId, Integer quantity);

    List<WishItem> getAllWishItems(Long userId);

    WishItem updateWishItem(Integer quantity, Long itemId, Long userId);

    WishItem deleteWishItem(Long userId, Long itemId);
}
