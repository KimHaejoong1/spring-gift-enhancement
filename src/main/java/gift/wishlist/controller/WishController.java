package gift.wishlist.controller;

import gift.jwt.auth.LoginMember;
import gift.member.model.Member;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.model.Wish;
import gift.wishlist.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Wish> addWish(
            @Valid @RequestBody WishRequestDto requestDto,
            @LoginMember Member member) {

        Wish wish = wishService.addWish(member.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(wish);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishes(@LoginMember Member member) {
        List<WishResponseDto> wishes = wishService.getWishesByMemberId(member.getId());
        return ResponseEntity.ok(wishes);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember Member member) {

        wishService.deleteWish(wishId, member.getId());
        return ResponseEntity.noContent().build();
    }
}
