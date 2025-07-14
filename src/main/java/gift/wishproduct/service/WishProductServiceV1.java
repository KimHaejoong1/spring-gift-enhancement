package gift.wishproduct.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishProduct;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.service.MemberService;
import gift.product.service.ProductService;
import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.dto.WishProductResponse;
import gift.wishproduct.dto.WishProductUpdateReq;
import gift.wishproduct.repository.WishProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishProductServiceV1 implements WishProductService {

    private final WishProductRepository wishProductRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public WishProductServiceV1(WishProductRepository wishProductRepository, ProductService productService, MemberService memberService) {
        this.wishProductRepository = wishProductRepository;
        this.productService = productService;
        this.memberService = memberService;
    }


    @Override
    public UUID save(WishProductCreateReq dto, String email) {

        Product product = productService.findById(dto.getProductId());

        Member owner = memberService.findByEmail(email);

        WishProduct wishProduct = wishProductRepository.findByOwnerIdAndProductId(owner.getId(), product.getId())
                .orElse(null);

        if (wishProduct == null) {
            WishProduct saved = wishProductRepository.save(new WishProduct(dto.getQuantity(), owner.getId(), product.getId()));

            return saved.getId();
        }

        wishProductRepository.update(new WishProduct(wishProduct.getId(), wishProduct.getQuantity()+dto.getQuantity(), owner.getId(), product.getId()));

        return wishProduct.getId();
    }

    @Override
    public List<WishProductResponse> findByEmail(String email) {

        Member owner = memberService.findByEmail(email);

        return wishProductRepository.findWithProductByOwnerId(owner.getId());
    }

    @Override
    public void deleteById(UUID id, String email) {

        WishProduct wishProduct = wishProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 위시 상품입니다."));

        Member owner = memberService.findByEmail(email);

        if (!owner.getId().equals(wishProduct.getOwnerId()))
            throw new BadRequestEntityException("자신의 위시 상품만 삭제할 수 있습니다");

        wishProductRepository.deleteById(wishProduct.getId());
    }

    @Override
    public void updateQuantity(UUID id, WishProductUpdateReq dto, String email) {

        WishProduct wishProduct = wishProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 위시 상품입니다."));

        Member owner = memberService.findByEmail(email);

        if (!owner.getId().equals(wishProduct.getOwnerId()))
            throw new BadRequestEntityException("자신의 위시 상품만 수정할 수 있습니다");

        wishProductRepository.update(new WishProduct(
                wishProduct.getId(), dto.getQuantity(),
                owner.getId(), wishProduct.getId()
        ));

    }


}
