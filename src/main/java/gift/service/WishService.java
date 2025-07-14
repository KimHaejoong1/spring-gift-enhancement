package gift.service;

import gift.domain.Wish;
import gift.exception.AlreadyWishedException;
import gift.exception.UnauthorizedWishAccessException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    /**
     * 위시리스트에 상품 추가
     * 이미 찜한 경우 예외 발생
     */
    public Wish addWish(Long memberId, Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        if (wishRepository.existsInWishlist(memberId, productId)) {
            throw new AlreadyWishedException();
        }

        return wishRepository.addWish(memberId, productId);
    }

    /**
     * 위시리스트에서 상품 제거
     * 멱등성 보장: 존재하지 않아도 예외 없이 삭제 시도
     * 다른 사용자의 찜 항목은 삭제할 수 없음 (wishId + memberId 조건 사용)
     */
    public void removeWish(Long wishId, Long memberId) {

        wishRepository.findById(wishId).ifPresent(wish -> {
            if (!wish.getMemberId().equals(memberId)) {
                throw new UnauthorizedWishAccessException("다른 사용자의 위시리스트 항목은 삭제할 수 없습니다.");
            }
            wishRepository.removeByMemberIdAndWishId(memberId, wishId);
        });

        // 존재하지 않으면 무시 (멱등성 보장)
    }


    /**
     * 사용자별 위시리스트 조회
     * 존재하지 않을 경우 빈 리스트 반환
     */
    public List<Wish> getWishlist(Long memberId) {
        return wishRepository.getWishlistByMemberId(memberId);
    }
}
