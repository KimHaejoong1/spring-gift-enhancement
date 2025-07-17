package gift.repository;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.entity.WishList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishlistRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WishlistRepository wishlistRepository;

    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        member = new Member("test@example.com", "password", Role.USER);
        product = new Product("휠렛버거", BigInteger.valueOf(5000), "https://example.com/image.jpg");

        entityManager.persist(member);
        entityManager.persist(product);
        entityManager.flush();
    }

    @Test
    void save_success() {
        WishList wishlist = new WishList(member, product, 2);

        WishList saved = wishlistRepository.save(wishlist);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMember()).isEqualTo(member);
        assertThat(saved.getProduct()).isEqualTo(product);
        assertThat(saved.getQuantity()).isEqualTo(2);
    }

    @Test
    void findByMember_success() {
        WishList wishlist1 = new WishList(member, product, 1);

        Product product2 = new Product("딥치즈버거", BigInteger.valueOf(5500), "https://example.com/2.jpg");
        entityManager.persist(product2);
        WishList wishlist2 = new WishList(member, product2, 3);

        entityManager.persist(wishlist1);
        entityManager.persist(wishlist2);
        entityManager.flush();

        List<WishList> wishlists = wishlistRepository.findByMember(member);

        assertThat(wishlists).hasSize(2);
        assertThat(wishlists).extracting("quantity").containsExactlyInAnyOrder(1, 3);
    }

    @Test
    void findByMember_empty() {
        Member otherMember = new Member("other@example.com", "password", Role.USER);
        entityManager.persistAndFlush(otherMember);

        List<WishList> wishlists = wishlistRepository.findByMember(otherMember);

        assertThat(wishlists).isEmpty();
    }

    @Test
    void findByMemberAndProduct_success() {
        WishList wishlist = new WishList(member, product, 5);
        entityManager.persistAndFlush(wishlist);

        Optional<WishList> found = wishlistRepository.findByMemberAndProduct(member, product);

        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(5);
        assertThat(found.get().getMember()).isEqualTo(member);
        assertThat(found.get().getProduct()).isEqualTo(product);
    }

    @Test
    void findByMemberAndProduct_not_found() {
        Product otherProduct = new Product("딥치즈버거", BigInteger.valueOf(5500), "https://example.com/other.jpg");
        entityManager.persistAndFlush(otherProduct);

        Optional<WishList> found = wishlistRepository.findByMemberAndProduct(member, otherProduct);

        assertThat(found).isEmpty();
    }

    @Test
    void delete_success() {
        WishList wishlist = new WishList(member, product, 1);
        WishList saved = entityManager.persistAndFlush(wishlist);

        wishlistRepository.delete(saved);

        Optional<WishList> found = wishlistRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void findById_success() {
        WishList wishlist = new WishList(member, product, 4);
        WishList saved = entityManager.persistAndFlush(wishlist);

        Optional<WishList> found = wishlistRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getQuantity()).isEqualTo(4);
    }
}
