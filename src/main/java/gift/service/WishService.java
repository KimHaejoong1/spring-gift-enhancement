package gift.service;

import gift.config.UnAuthorizationException;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.*;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    public WishService(ProductRepository productRepository, WishRepository wishRestController) {
        this.productRepository = productRepository;
        this.wishRepository = wishRestController;
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public CreateWishResponse addWishProduct(CreateWishRequest request, Long loginMemberId) {
        Wish wish = wishRepository.save(request.productId(), loginMemberId, request.quantity());
        return new CreateWishResponse(wish.getId(), wish.getMemberId(), wish.getProductId(), wish.getQuantity());
    }

    public List<WishResponse> getMeberWishList(Long memberId) {
        return wishRepository.findAllByMember(memberId);
    }

    public void delete(Long wishId, Long memberId) {
        Wish wishProduct = findByIdOrThrow(wishId);
        checkAuthorization(wishProduct, memberId, "삭제 권한 없음");
        wishRepository.delete(wishId);
    }

    public UpdateWishResponse updateQuantity(UpdateWishRequest request, Long wishId, Long memberId) {
        Wish wishProduct = findByIdOrThrow(wishId);
        checkAuthorization(wishProduct, memberId, "수정 권한 없음");

        wishRepository.update(request.quantity(), wishId);
        return new UpdateWishResponse(wishProduct.getId(), wishProduct.getProductId(), request.quantity());
    }

    private static void checkAuthorization(Wish wishProduct, Long memberId, String exMessage) {
        if (!wishProduct.getMemberId().equals(memberId)) {
            throw new UnAuthorizationException(exMessage);
        }
    }

    private Wish findByIdOrThrow(Long wishId) {
        Optional<Wish> findWishProduct = wishRepository.findById(wishId);
        if (findWishProduct.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 위시리스트 상품");
        }
        return findWishProduct.get();
    }
}
