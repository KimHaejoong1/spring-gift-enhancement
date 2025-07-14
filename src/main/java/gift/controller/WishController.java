package gift.controller;

import gift.common.annotation.CurrentUser;
import gift.common.code.CustomResponseCode;
import gift.common.dto.CustomResponseBody;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.User;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
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
    public ResponseEntity<CustomResponseBody<WishResponse>> addWish(
        @RequestBody @Valid WishRequest request,
        @CurrentUser User user
    ) {
        WishResponse response = wishService.addWish(user.getId(), request);
        return ResponseEntity
            .status(CustomResponseCode.CREATED.getHttpStatus())
            .body(CustomResponseBody.of(CustomResponseCode.CREATED, response));
    }

    @GetMapping
    public ResponseEntity<CustomResponseBody<List<WishResponse>>> getWishes(
        @CurrentUser User user
    ) {
        List<WishResponse> wishes = wishService.getWishes(user.getId());
        return ResponseEntity
            .ok(CustomResponseBody.of(CustomResponseCode.RETRIEVED, wishes));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CustomResponseBody<Void>> deleteWish(
        @PathVariable Long productId,
        @CurrentUser User user
    ) {
        wishService.deleteWish(user.getId(), productId);
        return ResponseEntity
            .status(CustomResponseCode.DELETED.getHttpStatus())
            .body(CustomResponseBody.of(CustomResponseCode.DELETED));
    }
}
