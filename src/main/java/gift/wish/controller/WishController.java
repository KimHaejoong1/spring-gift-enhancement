package gift.wish.controller;

import gift.auth.LoginMember;
import gift.member.entity.Member;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.wish.service.WishService;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/wishlists")
public class WishController {

    private final WishService wishService;
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishResponseDto>>> getWishlist(@LoginMember Member member) {
        Wish wish = new Wish(null, member.getId(),null,null);
        WishRequestDto wishRequestDto = WishRequestDto.fromEntity(wish);
        return ResponseEntity.ok(new ApiResponse<>(200,"조회에 성공했습니다", wishService.getWishlist(wishRequestDto)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishResponseDto>> addWish(@RequestBody WishRequestDto dto,
                                                                @LoginMember Member member) {

        dto.setMemberId(member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"추가에 성공했습니다", wishService.addWish(dto)));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteWish(@PathVariable Long productId,
                                                        @LoginMember Member member) {
        Wish wish = new Wish(null, member.getId(),productId,null);
        WishRequestDto dto = WishRequestDto.fromEntity(wish);
        wishService.deleteWish(dto);
        return ResponseEntity.ok(new ApiResponse<>(200,"삭제에 성공했습니다", null));
    }

}
