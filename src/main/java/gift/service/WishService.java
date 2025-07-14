package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class WishService {

    public final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void saveWish(long memberId, long productId, int count) {
        if (count == 0){
            return;
        }

        wishRepository.saveWish(memberId, productId, count);
    }

    public void updateWishCount(long memberId, long productId, int count) {
        if (count == 0) {
            wishRepository.deleteWish(memberId, productId);
            return;
        }

        wishRepository.updateWishCount(memberId, productId, count);
    }

    @Transactional(readOnly = true)
    public List<WishResponseDto> getWishList(long memberId) {
        return wishRepository.getWishProductsByMemberId(memberId)
                .stream().map(
                    wish ->
                        new WishResponseDto(
                                wish.getCount(),
                                new ProductResponseDto(
                                        wish.getProduct()
                                )
                        )
                ).toList();
    }
}
