package gift.service.wishListService;

import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import jakarta.validation.Valid;

import java.util.List;

public interface WishListService {

    ResponseWishItemDto addWishItem(@Valid AddWishItemDto dto, String userEmail);

    List<ResponseWishItemDto> getItemList(String name, Integer price, String userEmail);

    ResponseWishItemDto deleteWishItem(String name, String userEmail);

    ResponseWishItemDto updateWishItem(Integer quantity, String name, String userEmail);
}
