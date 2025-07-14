package gift.wishlist.service.impl;

import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.exception.DuplicatedWishException;
import gift.wishlist.exception.WishNotAuthorityException;
import gift.wishlist.exception.WishNotFoundException;
import gift.wishlist.model.Wish;
import gift.wishlist.repository.WishRepository;
import gift.wishlist.service.WishService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public Wish addWish(Long memberId, WishRequestDto requestDto) {
        if (wishRepository.exists(memberId, requestDto.productId())) {
            throw new DuplicatedWishException("이미 위시리스트에 등록된 상품입니다.");
        }

        Wish wish = requestDto.toEntity(memberId);
        return wishRepository.save(wish);
    }

    @Override
    public List<WishResponseDto> getWishesByMemberId(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);

        return wishes.stream()
                .map(WishResponseDto::from)
                .toList();
    }

    @Override
    public void deleteWish(Long productId, Long memberId) {
        Wish wish = wishRepository.findByProductId(productId)
                .orElseThrow(() -> new WishNotFoundException("위시리스트 항목을 찾을 수 없습니다."));

        if (!wish.getMemberId().equals(memberId)) {
            throw new WishNotAuthorityException("위시리스트에 대한 권한이 없습니다.");
        }

        wishRepository.deleteById(productId);
    }
}
