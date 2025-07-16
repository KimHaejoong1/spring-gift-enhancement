package gift.service;

import gift.dto.WishlistRequestDTO;
import gift.dto.WishlistResponseDTO;
import gift.entity.Member;
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
    private final MemberService memberService;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository, MemberService memberService) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberService = memberService;
    }

    @Transactional
    public WishlistResponseDTO addWishlist(Integer memberId, WishlistRequestDTO wishlistRequestDTO) {
        Member member = memberService.getMemberEntityById(memberId);
        Product product = productRepository.findById(wishlistRequestDTO.productId());

        Optional<WishList> existing = wishlistRepository.findByMemberAndProduct(member, product);

        if (existing.isPresent()) {
            WishList wishlist = existing.get();
            Integer newQuantity = wishlist.getQuantity() + wishlistRequestDTO.quantity();
            return updateQuantity(wishlist.getId(), newQuantity, memberId);
        }

        WishList wishlist = new WishList(member, product, wishlistRequestDTO.quantity());
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
        Member member = memberService.getMemberEntityById(memberId);
        List<WishList> wishlists = wishlistRepository.findByMember(member);

        return wishlists.stream()
                .map(wishList -> new WishlistResponseDTO(
                        wishList.getId(),
                        wishList.getProduct().getId(),
                        wishList.getProduct().getName(),
                        wishList.getProduct().getPrice(),
                        wishList.getProduct().getImageUrl(),
                        wishList.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public WishlistResponseDTO updateQuantity(Integer wishlistId, Integer quantity, Integer memberId) {
        WishList wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트를 찾을 수 없습니다."));
        if (!wishlist.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("해당 위시리스트에 접근할 권한이 없습니다.");
        }

        wishlist.setQuantity(quantity);

        Product product = wishlist.getProduct();

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

        if (!wishlist.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("해당 위시리스트에 접근할 권한이 없습니다.");
        }
        wishlistRepository.delete(wishlist);
    }
}
