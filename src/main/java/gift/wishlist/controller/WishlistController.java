package gift.wishlist.controller;

import gift.common.annotation.LoginMember;
import gift.wishlist.dto.WishlistRequestDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // 위시 리스트에 상품 추가 API
    @PostMapping
    public ResponseEntity<WishlistResponseDto> addProductToWishlist(
            @RequestBody WishlistRequestDto requestDto,
            @LoginMember Long memberId) {

        WishlistResponseDto responseDto = wishlistService.addProductToWishlist(memberId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 위시 리스트 상품 목록 조회 API
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(@LoginMember Long memberId) {
        List<WishlistResponseDto> productsOfWishlist = wishlistService.getWishlist(memberId);

        return ResponseEntity.ok(productsOfWishlist);
    }

    // 위시 리스트에 있는 상품 삭제 API
    @DeleteMapping("/{productId}")
    public ResponseEntity<WishlistResponseDto> deleteProductFromWishlist(
            @PathVariable Long productId,
            @LoginMember Long memberId
    ) {
        WishlistResponseDto responseDto = wishlistService.deleteProductFromWishlist(memberId, productId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 위시 리스트의 상품 수량 수정 API
    @PatchMapping
    public ResponseEntity<WishlistResponseDto> updateProductQuantity(
            @LoginMember Long memberId,
            @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.updateProductQuantity(memberId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }




}
