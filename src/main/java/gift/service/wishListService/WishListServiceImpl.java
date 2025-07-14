package gift.service.wishListService;

import gift.dto.itemDto.ItemResponseDto;
import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.entity.Item;
import gift.entity.User;
import gift.entity.WishItem;
import gift.exception.itemException.ItemNotFoundException;
import gift.exception.itemException.UserInputException;
import gift.exception.userException.UserNotFoundException;
import gift.repository.wishListRepository.WishListRepository;
import gift.service.itemService.ItemService;
import gift.service.userService.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;
    private final UserService userService;
    private final ItemService itemService;

    public WishListServiceImpl(WishListRepository wishListRepository, UserService userService, ItemService itemService) {
        this.wishListRepository = wishListRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public ResponseWishItemDto addWishItem(AddWishItemDto dto, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        ItemResponseDto item = itemService.findItemByName(dto.name());
        if (item == null) {
            throw new ItemNotFoundException(dto.name());
        }

        Integer quantity = dto.quantity();
        WishItem addedWishItem = wishListRepository.addWishItem(user.id(), item.id(), quantity);


        return ResponseWishItemDto.from(addedWishItem);
    }

    @Override
    public List<ResponseWishItemDto> getItemList(String name, Integer price, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<WishItem> wishItems = wishListRepository.getAllWishItems(user.id());
        if (wishItems.isEmpty()) {
            throw new ItemNotFoundException();
        }

        List<ResponseWishItemDto> result = getWishItems(wishItems, name, price);

        return result;
    }
    private boolean isValid(ItemResponseDto item, String name, Integer price) {
        boolean nameMatches = (name == null || item.name().equals(name));
        boolean priceMatches = (price == null || item.price().equals(price));

        return nameMatches && priceMatches;
    }

    private List<ResponseWishItemDto> getWishItems(List<WishItem> wishItems, String name, Integer price) {
        List<ResponseWishItemDto> result = new ArrayList<>();

        for (WishItem wishItem : wishItems) {
            ItemResponseDto item = itemService.findItemById(wishItem.itemId());
            if (item == null) {
                if (name == null && price == null) {
                    throw new UserInputException();
                }
                continue;
            }

            if (name == null && price == null || isValid(item, name, price)) {
                result.add(ResponseWishItemDto.from(wishItem));
            }
        }

        return result;
    }

    @Override
    public ResponseWishItemDto deleteWishItem(String name, String userEmail) {
        User user = userService.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        ItemResponseDto item = itemService.findItemByName(name);

        if (item == null) {
            throw new ItemNotFoundException();
        }

        WishItem deletedWishItem = wishListRepository.deleteWishItem(user.id(), item.id());

        return ResponseWishItemDto.delete(deletedWishItem);
    }

    @Override
    public ResponseWishItemDto updateWishItem(Integer quantity, String name, String userEmail) {
        User user = userService.findUserByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException();
        }

        ItemResponseDto item = itemService.findItemByName(name);

        if (item == null) {
            throw new ItemNotFoundException();
        }

        WishItem updateItem = wishListRepository.updateWishItem(quantity, item.id(), user.id());

        if (updateItem == null) {
            throw new ItemNotFoundException();
        }

        return ResponseWishItemDto.from(updateItem);
    }
}
