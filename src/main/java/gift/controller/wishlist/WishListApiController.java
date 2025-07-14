package gift.controller.wishlist;

import gift.dto.IdResponse;
import gift.dto.wishlist.WishListRequest;
import gift.dto.wishlist.WishListResponse;
import gift.global.util.RequestAttributes;
import gift.service.wishlist.WishListService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlists")
public class WishListApiController {

    private final WishListService wishListService;

    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // wishlist 조회
    // 특정 유저의 위시리스트를 전부 조회한다.(API를 요청한 유저의 위시리스트를 조회)
    @GetMapping
    public ResponseEntity<?> getWishList(
        @RequestAttribute(RequestAttributes.MEMBER_ID) Long memberId
    ) {
        List<WishListResponse> list = wishListService.findWishListAllById(memberId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    // wishList 단건 조회: memberId, productId로 조회
    @GetMapping("/{productId}")
    public ResponseEntity<?> getWishListById(
        @PathVariable Long productId,
        @RequestAttribute(RequestAttributes.MEMBER_ID) Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(wishListService.findWishListByMemberAndProduct(memberId, productId));
    }


    // wishlist 수정(추가, 삭제)
    // 수량을 변경할 productId와 수량을 requestBody에 넣어서 전달받음
    // 레코드가 없으면 생성 후 수량 반영
    // 수량은 음수도 받을 수 있고, 수정 후 수량이 음수가 되면 0으로 수정해서 저장하고 있음.
    @PostMapping("/update")
    public ResponseEntity<?> updateWishList(
        @RequestAttribute(RequestAttributes.MEMBER_ID) Long memberId,
        @RequestBody WishListRequest wishListRequest
    ) {
        // 본인 위시리스트 중 특정 productId의 수량을 변경하는 요청
        Long wishListId = wishListService.update(memberId, wishListRequest);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new IdResponse(wishListId));
    }

    // quantity 수정과 별개로, 위시리스트 테이블에 저장된 레코드 자체를 삭제
    @PostMapping("/delete")
    public ResponseEntity<?> deleteWishList(
        @RequestAttribute(RequestAttributes.MEMBER_ID) Long memberId,
        @RequestBody WishListRequest wishListRequest
    ) {
        wishListService.delete(memberId, wishListRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
