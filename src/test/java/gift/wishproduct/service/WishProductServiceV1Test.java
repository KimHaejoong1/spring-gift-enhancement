package gift.wishproduct.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Role;
import gift.domain.WishProduct;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.service.MemberService;
import gift.product.service.ProductService;
import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.dto.WishProductResponse;
import gift.wishproduct.dto.WishProductUpdateReq;
import gift.wishproduct.repository.WishProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class WishProductServiceV1Test {

    @InjectMocks
    private WishProductServiceV1 wishProductService;

    @Mock
    private WishProductRepository wishProductRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ProductService productService;

    @Test
    @DisplayName("위시 상품 추가 성공 - 새로운 위시 상품")
    void addWishProductSuccess() {

        // given
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProductCreateReq dto = new WishProductCreateReq(product.getId(), 10);
        WishProduct wishProduct = addWishProduct(product, member, dto.getQuantity());


        given(productService.findById(product.getId()))
            .willReturn(product);

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(member);

        given(wishProductRepository.findByOwnerIdAndProductId(member.getId(), product.getId()))
                .willReturn(Optional.empty());

        given(wishProductRepository.save(any(WishProduct.class)))
                .willReturn(wishProduct);


        // when
        UUID savedId = wishProductService.save(dto, member.getEmail());

        // then
        assertThat(savedId).isEqualTo(wishProduct.getId());
        verify(wishProductRepository).save(any(WishProduct.class));
        verify(productService).findById(product.getId());
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).findByOwnerIdAndProductId(member.getId(), product.getId());
        verifyNoMoreInteractions(wishProductRepository, productService, memberService);
    }

    @Test
    @DisplayName("위시 상품 추가 성공 - 기존 위시 상품에 수량 추가")
    void updateWishProductSuccess() {
        // given
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProductCreateReq dto = new WishProductCreateReq(product.getId(), 10);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        given(productService.findById(product.getId()))
                .willReturn(product);

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(member);

        given(wishProductRepository.findByOwnerIdAndProductId(member.getId(), product.getId()))
                .willReturn(Optional.of(wishProduct));

        // when
        UUID updatedId = wishProductService.save(dto, member.getEmail());

        // then
        assertThat(updatedId).isEqualTo(wishProduct.getId());
        verify(productService).findById(product.getId());
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).update(any(WishProduct.class));
        verify(wishProductRepository).findByOwnerIdAndProductId(member.getId(), product.getId());
        verifyNoMoreInteractions(wishProductRepository, productService, memberService);
    }

    @Test
    @DisplayName("위시 상품 추가 실패 - 존재하지 않는 회원")
    void addWishProductFail() {
        // given
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProductCreateReq dto = new WishProductCreateReq(product.getId(), 10);


        given(productService.findById(product.getId()))
                .willReturn(product);

        given(memberService.findByEmail(member.getEmail()))
                .willThrow(new NotFoundEntityException("존재하지 않는 회원입니다."));

        // when & then
        assertThatThrownBy(() -> wishProductService.save(dto, member.getEmail()))
                .isInstanceOf(NotFoundEntityException.class);
        verify(productService).findById(product.getId());
        verify(memberService).findByEmail(member.getEmail());
        verifyNoMoreInteractions(wishProductRepository, productService, memberService);

    }

    @Test
    @DisplayName("위시 상품 추가 실패 - 존재하지 않는 상품")
    void addWishProductFail2() {
        // given
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProductCreateReq dto = new WishProductCreateReq(product.getId(), 10);


        given(productService.findById(product.getId()))
                .willThrow(new NotFoundEntityException("존재하지 않는 상품입니다."));


        // when & then
        assertThatThrownBy(() -> wishProductService.save(dto, member.getEmail()))
                .isInstanceOf(NotFoundEntityException.class);
        verify(productService).findById(product.getId());
        verifyNoMoreInteractions(wishProductRepository, productService, memberService);

    }

    @Test
    @DisplayName("위시 상품 조회")
    void getWishProductSuccess() {
        // given
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(member);

        given(wishProductRepository.findWithProductByOwnerId(member.getId()))
                .willReturn(List.of(new WishProductResponse(wishProduct.getId(), product.getName(),
                        product.getPrice(), wishProduct.getQuantity(), product.getImageURL())));

        // when
        List<WishProductResponse> result = wishProductService.findByEmail(member.getEmail());

        // then
        assertThat(result.size()).isEqualTo(1);
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).findWithProductByOwnerId(member.getId());
        verifyNoMoreInteractions(wishProductRepository, memberService, productService);

    }

    @Test
    @DisplayName("위시 상품 삭제 성공")
    void deleteWishProductSuccess() {
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        // given

        given(wishProductRepository.findById(wishProduct.getId()))
                .willReturn(Optional.of(wishProduct));

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(member);

        // when
        wishProductService.deleteById(wishProduct.getId(), member.getEmail());


        // then
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).findById(wishProduct.getId());
        verify(wishProductRepository).deleteById(wishProduct.getId());
        verifyNoMoreInteractions(wishProductRepository, memberService, productService);
    }

    @Test
    @DisplayName("위시 상품 삭제 성공 - 자신의 위시 상품이 아님")
    void deleteWishProductFail() {
        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        // given

        given(wishProductRepository.findById(wishProduct.getId()))
                .willReturn(Optional.of(wishProduct));

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(new Member("temp@naver.com", "Qwer1234!!", Role.REGULAR));

        // when
        assertThatThrownBy(()->wishProductService.deleteById(wishProduct.getId(), member.getEmail()))
                .isInstanceOf(BadRequestEntityException.class);


        // then
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).findById(wishProduct.getId());
        verifyNoMoreInteractions(wishProductRepository, memberService, productService);
    }

    @Test
    @DisplayName("위시 상품 수량 수정 성공")
    void changeQuantitySuccess() {
        // given

        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        given(wishProductRepository.findById(wishProduct.getId()))
                .willReturn(Optional.of(wishProduct));

        given(memberService.findByEmail(member.getEmail()))
                .willReturn(member);

        // when

        wishProductService.updateQuantity(wishProduct.getId(), new WishProductUpdateReq(10), member.getEmail());


        // then

        verify(wishProductRepository).findById(wishProduct.getId());
        verify(memberService).findByEmail(member.getEmail());
        verify(wishProductRepository).update(any(WishProduct.class));
        verifyNoMoreInteractions(wishProductRepository, memberService, productService);
    }

    @Test
    @DisplayName("위시 상품 수량 수정 실패 - 자신의 위시 상품이 아님")
    void changeQuantityFail() {
        // given

        Member member = addMemberCase();
        Product product = addProductCase(member);
        WishProduct wishProduct = addWishProduct(product, member, 15);

        given(wishProductRepository.findById(wishProduct.getId()))
                .willReturn(Optional.of(wishProduct));

        given(memberService.findByEmail(anyString()))
                .willReturn(new Member("temp@naver.com", "Qwer1234!!", Role.REGULAR));

        // when

        assertThatThrownBy(()->wishProductService.updateQuantity(wishProduct.getId(),
                new WishProductUpdateReq(10), member.getEmail())
        ).isInstanceOf(BadRequestEntityException.class);


        // then

        verify(wishProductRepository).findById(wishProduct.getId());
        verify(memberService).findByEmail(member.getEmail());
        verifyNoMoreInteractions(wishProductRepository, memberService, productService);
    }



    private Member addMemberCase() {
        return new Member("ljw2109@naver.com", "Qwer1234!!", Role.REGULAR);
    }

    private Product addProductCase(Member member) {
        return new Product("스윙칩",3000, "data:image/~base64,",member.getId());
    }

    private WishProduct addWishProduct(Product product, Member member, int quantity) {
        return new WishProduct(quantity,
                member.getId(), product.getId());
    }

}