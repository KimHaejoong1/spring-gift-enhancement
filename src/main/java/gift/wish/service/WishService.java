package gift.wish.service;

import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<WishResponseDto> getWishlist(WishRequestDto dto) {
        return wishRepository.getWishList(dto).stream()
                .map(WishResponseDto::fromEntity).toList();
    }

    public WishResponseDto addWish(WishRequestDto dto) {
        List<Wish> list = wishRepository.getWishList(dto);
        List<Long> productIds = list.stream().map(Wish::getProductId).toList();
        if(productIds.stream().anyMatch(dto.getProductId()::equals)){
            throw new IllegalArgumentException("이미 추가 되어있습니다!");
        }


        return wishRepository.addWish(dto);
    }

    public void deleteWish(WishRequestDto dto) {
        wishRepository.deleteWish(dto);
    }

}
