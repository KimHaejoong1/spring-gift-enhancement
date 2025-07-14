package gift.service;

import gift.dto.WishlistRequestDTO;
import gift.dto.WishlistResponseDTO;
import gift.entity.Product;
import gift.entity.WishList;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishlistResponseDTO addWishlist(Integer memberId, WishlistRequestDTO wishlistRequestDTO) {
        Product product = productRepository.findById(wishlistRequestDTO.productId());

        Optional<WishList> existing = wishlistRepository.findByMemberIdAndProductId(memberId, wishlistRequestDTO.productId());

        if (existing.isPresent()) {
            WishList wishlist = existing.get();
            Integer newQuantity = wishlist.getQuantity() + wishlistRequestDTO.quantity();
            return updateQuantity(wishlist.getId(), newQuantity, memberId);
        }

        WishList wishlist = new WishList(null, memberId, wishlistRequestDTO.productId(), wishlistRequestDTO.quantity());
        WishList saved = wishlistRepository.save(wishlist);

        return new WishlistResponseDTO(
                saved.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                saved.getQuantity()
        );
    }

    public List<WishlistResponseDTO> getAllWishlistByMemberId(Integer memberId) {
        List<WishList> wishlists = wishlistRepository.findByMemberId(memberId);

        return wishlists.stream()
                .map(wishList -> {
                    Product product = productRepository.findById(wishList.getProductId());
                    return new WishlistResponseDTO(
                        wishList.getId(),
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        wishList.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public WishlistResponseDTO updateQuantity(Integer wishlistId, Integer quantity, Integer memberId) {
        WishList wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트를 찾을 수 없습니다."));
        if (!wishlist.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("해당 위시리스트에 접근할 권한이 없습니다.");
        }

        wishlistRepository.updateQuantity(wishlistId, quantity);

        Product product = productRepository.findById(wishlist.getProductId());

        return new WishlistResponseDTO(
                wishlistId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                quantity
        );
    }

    @Transactional
    public void deleteWishlist(Integer wishlistId, Integer memberId) {
        WishList wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트를 찾을 수 없습니다."));

        if (!wishlist.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("해당 위시리스트에 접근할 권한이 없습니다.");
        }
        wishlistRepository.deleteById(wishlistId);
    }
}
