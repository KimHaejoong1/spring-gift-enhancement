package gift;


import gift.exception.WishNotFoundByMemberIdAndWishId;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(WishlistRepository.class)
public class WishlistRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private WishlistRepository repository;

    @BeforeEach
    void setUp() {
        repository = new WishlistRepository(jdbcClient);
    }

    @Test
    void saveWish_테스트_새로운_위시리스트_저장() {
        Long memberId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Wishlist saved = repository.saveWish(memberId, productId, quantity);

        assertThat(saved).isNotNull();
        assertThat(saved.getMemberId()).isEqualTo(memberId);
        assertThat(saved.getProductId()).isEqualTo(productId);
        assertThat(saved.getQuantity()).isEqualTo(quantity);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveWish_테스트_동일상품_여러번_저장시_수량증가() {
        Long memberId = 1L;
        Long productId = 1L;

        repository.saveWish(memberId, productId, 2);
        repository.saveWish(memberId, productId, 3);

        Optional<Wishlist> found = repository.findWishByMemberIdAndProductId(memberId, productId);
        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(5);
    }

    @Test
    void findAllByMemberId_회원별_위시리스트_전체조회() {
        Long memberId = 1L;
        repository.saveWish(memberId, 1L, 1);
        repository.saveWish(memberId, 2L, 2);

        List<Wishlist> wishlists = repository.findAllByMemberId(memberId);

        assertThat(wishlists).hasSize(2);
        assertThat(wishlists)
                .extracting(Wishlist::getProductId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void deleteWishByMemberIdAndWishId_삭제_성공() {
        Long memberId = 1L;
        Long productId = 1L;
        Wishlist savedWish = repository.saveWish(memberId, productId, 1);

        repository.deleteWishByMemberIdAndWishId(memberId, savedWish.getId());

        Optional<Wishlist> found = repository.findWishByMemberIdAndProductId(memberId, productId);
        assertThat(found).isEmpty();
    }

    @Test
    void deleteWishByMemberIdAndWishId_삭제_실패_존재하지않는위시() {
        Long memberId = 1L;
        Long notExistWishId = 999L;

        assertThatThrownBy(() -> repository.deleteWishByMemberIdAndWishId(memberId, notExistWishId))
                .isInstanceOf(WishNotFoundByMemberIdAndWishId.class);
    }

    @Test
    void saveWish_동시성_테스트() {
        Long memberId = 1L;
        Long productId = 1L;

        repository.saveWish(memberId, productId, 1);
        repository.saveWish(memberId, productId, 1);
        repository.saveWish(memberId, productId, 1);

        Optional<Wishlist> found = repository.findWishByMemberIdAndProductId(memberId, productId);
        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(3);
    }
}