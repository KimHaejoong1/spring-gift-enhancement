package gift.api.wishlist.controller;

import gift.api.wishlist.dto.WishlistRequestDto;
import gift.api.wishlist.dto.WishlistResponseDto;
import gift.api.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/wishes"))
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(
            @RequestAttribute("userEmail") String email,
            @PageableDefault(size = 5, sort = "created_date", direction = Sort.Direction.DESC) Pageable pageable) {
        List<WishlistResponseDto> wishlist = wishlistService.getWishlist(email, pageable)
                .getContent();

        return ResponseEntity.ok(wishlist);
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDto> addProductToWishlist(
            @RequestAttribute("userEmail") String email,
            @Valid @RequestBody WishlistRequestDto wishlistRequestDto
    ) {
        WishlistResponseDto wishlistResponseDto = wishlistService.addProductToWishlist(email,
                wishlistRequestDto.productId());

        URI location = URI.create("/members/products/" + wishlistResponseDto.product().id());

        return ResponseEntity.created(location).body(wishlistResponseDto);
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> removeProductFromWishlist(
            @RequestAttribute("userEmail") String email,
            @PathVariable Long wishlistId) {
        wishlistService.removeProductFromWishlist(email, wishlistId);

        return ResponseEntity.noContent().build();
    }
}
