package gift;


import gift.exception.ProductNotFoundException;
import gift.exception.WishNotFoundByMemberIdAndWishId;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.repository.WishlistRepository;
import gift.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {
    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Wishlist wishlist;

    @InjectMocks
    private WishlistService wishlistService;

    @Test
    void addWish_새로운위시등록후_Dto정상반환(){
        WishRequestDto request = new WishRequestDto(100L, 2);
        Product product = new Product(100L, "테스트상품", 5000L, "http://image.url", false);
        Wishlist savedWish = new Wishlist(1L, 1L, 100L, 2);

        given(productRepository.findById(request.productId()))
                .willReturn(Optional.of(product));
        given(wishlistRepository.saveWish(1L, request.productId(), request.quantity()))
                .willReturn(savedWish);

        WishResponseDto response = wishlistService.addWish(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.memberId()).isEqualTo(1L);
        assertThat(response.productId()).isEqualTo(100L);
        assertThat(response.name()).isEqualTo("테스트상품");
        assertThat(response.price()).isEqualTo(5000L);
        assertThat(response.imageUrl()).isEqualTo("http://image.url");
        assertThat(response.quantity()).isEqualTo(2);
    }

    @Test
    void addWish_상품없음_예외발생(){
        WishRequestDto request = new WishRequestDto(100L, 2);
        given(productRepository.findById(request.productId()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> wishlistService.addWish(1L, request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("100");
    }

    @Test
    void getWishesByMemberId_위시리스트가_비어있을_경우(){
        given(wishlistRepository.findAllByMemberId(1L))
                .willReturn(List.of());

        List<WishResponseDto> response = wishlistService.getWishesByMemberId(1L);

        assertThat(response).isEmpty();
    }

    @Test
    void getWishesByMemberId_위시리스트에_위시가_존재할_경우(){
        Wishlist wish1 = new Wishlist(1L, 1L, 10L, 3);
        Wishlist wish2 = new Wishlist(2L, 1L, 20L, 1);
        given(wishlistRepository.findAllByMemberId(1L))
                .willReturn(List.of(wish1, wish2));

        Product p1 = new Product(10L, "일반상품", 1000L, "http://image1.url", false);
        Product p2 = new Product(20L, "카카오상품", 2000L, "http://image2.url", true);
        given(productRepository.findAllByIdIn(List.of(10L, 20L)))
                .willReturn(List.of(p1, p2));

        List<WishResponseDto> dtos = wishlistService.getWishesByMemberId(1L);

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(WishResponseDto::productId)
                .containsExactlyInAnyOrder(10L, 20L);
        assertThat(dtos).extracting(WishResponseDto::name)
                .containsExactlyInAnyOrder("일반상품", "카카오상품");
    }

    @Test
    void deleteWish_정상적으로_위시삭제(){
        willDoNothing().given(wishlistRepository)
                .deleteWishByMemberIdAndWishId(1L, 5L);

        assertDoesNotThrow(() -> wishlistService.deleteWish(1L, 5L));
    }

    @Test
    void delteWish_존재하지_않는_위시_삭제시_repository예외_그대로_전파(){
        willThrow(new WishNotFoundByMemberIdAndWishId(1L, 123L))
                .given(wishlistRepository)
                .deleteWishByMemberIdAndWishId(1L, 123L);

        assertThatThrownBy(() -> wishlistService.deleteWish(1L, 123L))
                .isInstanceOf(WishNotFoundByMemberIdAndWishId.class);
    }
}
