package gift.controller.wishListController;

import gift.config.LoginUser;
import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItem;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.service.wishListService.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wish")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping
    public ResponseEntity<ResponseWishItemDto> addItem(@RequestBody @Valid AddWishItemDto dto, @LoginUser String userEmail) {

        ResponseWishItemDto addedWishItem = wishListService.addWishItem(dto, userEmail);

        return new ResponseEntity<>(addedWishItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseWishItem> getWishItemList(@LoginUser String userEmail, @RequestParam(required = false) String name, @RequestParam(required = false) Integer price) {
        List<ResponseWishItemDto> wishItemList = wishListService.getItemList(name, price, userEmail);
        ResponseWishItem response = new ResponseWishItem(wishItemList);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ResponseWishItemDto> deleteWishItem(@LoginUser String userEmail, @RequestParam String name) {

        ResponseWishItemDto deletedWishItem = wishListService.deleteWishItem(name, userEmail);

        return new ResponseEntity<>(deletedWishItem, HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<ResponseWishItemDto> updateWishItem(@LoginUser String userEmail, @RequestParam Integer quantity, @RequestParam String name) {

        ResponseWishItemDto updatedWishItem = wishListService.updateWishItem(quantity, name, userEmail);

        return new ResponseEntity<>(updatedWishItem, HttpStatus.ACCEPTED);
    }
}
