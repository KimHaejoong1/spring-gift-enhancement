package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.domain.Wish;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> getWishlist(@LoginMember Member member) {

        List<Wish> wishList = wishService.getWishlist(member.getId());

        List<WishResponse> response = wishList.stream()
                .map(WishResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WishResponse> addWish(
            @RequestBody @Valid WishRequest request,
            @LoginMember Member member
    ) {

        Wish wish = wishService.addWish(member.getId(), request.productId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WishResponse.from(wish));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeWish(
            @PathVariable Long wishId,
            @LoginMember Member member
    ) {
        wishService.removeWish(wishId, member.getId());
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
