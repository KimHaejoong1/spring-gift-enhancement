package gift.wishlist.service;

import gift.exception.ProductNotFoundException;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishResponseDto addWish(Long memberId, WishRequestDto wishRequestDto) {
        Product product = productRepository.findById(wishRequestDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(wishRequestDto.productId()));

        Wishlist wishlist = wishlistRepository.saveWish(
                memberId,
                wishRequestDto.productId(),
                wishRequestDto.quantity()
        );

        return new WishResponseDto(
                wishlist.getId(),
                wishlist.getMemberId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                wishlist.getQuantity()
        );
    }

    @Transactional(readOnly = true)
    public List<WishResponseDto> getWishesByMemberId(Long memberId) {
        List<Wishlist> wishes = wishlistRepository.findAllByMemberId(memberId);

        List<Long> ids = wishes.stream()
                .map(Wishlist::getProductId)
                .toList();

        if(ids.isEmpty()) {return List.of();}

        Map<Long, Product> productMap = productRepository.findAllByIdIn(ids)
                .stream()
                .collect(Collectors.toMap(product -> product.getId(), product -> product));

        return wishes.stream()
                .map(wish -> {
                    Product product = productMap.get(wish.getProductId());
                    return new WishResponseDto(
                            wish.getId(),
                            wish.getMemberId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl(),
                            wish.getQuantity()
                    );
                } )
                .toList();
    }

    @Transactional
    public void deleteWish(Long memberId, Long wishId) {
        wishlistRepository.deleteWishByMemberIdAndWishId(memberId, wishId);
    }
}
