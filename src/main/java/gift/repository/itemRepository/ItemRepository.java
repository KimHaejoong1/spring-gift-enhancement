package gift.repository.itemRepository;

import gift.dto.wishListDto.ResponseWishItemDto;
import gift.entity.Item;
import gift.entity.WishItem;

import java.util.List;

public interface ItemRepository {

    Item saveItem(Item item);

    List<Item> getItems(String name, Integer price);

    Item deleteItems(String name);

    Item findById(Long id);

    List<Item> getAllItems();

    Item deleteById(Long id);

    Item updateItem(Long id, String name, int price, String imageUrl);

    Item findItemByName(String name);

    Item findItemById(Long itemId);
}
