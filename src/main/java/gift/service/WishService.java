package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishCreateResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.Wish;
import gift.exception.ProductNotExistException;
import gift.exception.WishNotExistException;
import gift.exception.WishAlreadyExistException;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository,  ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    public List<WishResponseDto> getWishlist(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    ProductResponseDto productResponseDto = productService.find(wish.getProductId());
                    return new WishResponseDto(
                        wish.getId(),
                        productResponseDto
                    );
                })
                .collect(Collectors.toList());
    }

    public WishCreateResponseDto add(Long memberId, Long productId) {

        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new WishAlreadyExistException(productId);
        }

        if (!productService.exists(productId)) {
            throw new ProductNotExistException(productId);
        }

        Wish wish = wishRepository.save(memberId, productId);
        return new WishCreateResponseDto(wish.getId(), wish.getMemberId(), wish.getProductId());
    }

    public void remove(Long wishId) {

        boolean deleted = wishRepository.delete(wishId);
        if (!deleted) {
            throw new WishNotExistException(wishId);
        }
    }
}