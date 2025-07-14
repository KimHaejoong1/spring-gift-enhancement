package gift.service.wishlist;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishList;
import gift.dto.wishlist.WishListRequest;
import gift.dto.wishlist.WishListResponse;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishListRepository;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    // wishList 조회
    public List<WishListResponse> findWishListAllById(Long memberId) {
        List<WishList> list = wishListRepository.findWishListAllById(memberId);

        return list.stream()
            .map(each -> {
                Member member = memberRepository.findById(each.getMemberId());
                Product product = productRepository.findById(each.getProductId());

                return new WishListResponse(
                    member.getId(),
                    member.getEmail(),
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    each.getQuantity(),
                    product.getPrice() * each.getQuantity()
                );
            }).toList();

    }

    // wishlist 단건 조회
    public WishListResponse findWishListByMemberAndProduct(Long memberId, Long productId) {
        WishList wishList = wishListRepository.findWishListByMemberAndProduct(memberId, productId);

        Member member = memberRepository.findById(wishList.getMemberId());
        Product product = productRepository.findById(wishList.getProductId());

        return new WishListResponse(
            member.getId(),
            member.getEmail(),
            product.getId(),
            product.getName(),
            product.getPrice(),
            wishList.getQuantity(),
            product.getPrice() * wishList.getQuantity()
        );
    }

    public Long update(Long memberId, WishListRequest wishListRequest) {
        try {
            WishList wishList = wishListRepository.findWishListByMemberAndProduct(memberId,
                wishListRequest.productId());

            // 만약 수정 후 quantity 값이 음수라면 0으로 보정
            int requestQuantity = wishList.getQuantity() + wishListRequest.quantity();
            if (requestQuantity < 0) {
                requestQuantity = 0;
            }

            // update 호출할 때 계산된 quantity 값을 전달해야 함.
            wishListRepository.updateWishList(wishList.getId(), requestQuantity);

            return wishList.getId();
        } catch (DataAccessException e) {
            // 만약 찾는 위시리스트가 없으면 생성부터 해야 함.
            // 존재하지 않는 proudctId를 준 경우에는 예외 터지도록.
            productRepository.findById(wishListRequest.productId());

            return wishListRepository.insertWishList(memberId, wishListRequest.productId(),
                wishListRequest.quantity());
        }
    }

    public void delete(Long memberId, WishListRequest wishListRequest) {
        WishList wishList = wishListRepository.findWishListByMemberAndProduct(memberId,
            wishListRequest.productId());

        wishListRepository.deleteById(wishList.getId());
    }
}
