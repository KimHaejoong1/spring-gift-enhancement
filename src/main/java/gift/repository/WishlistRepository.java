package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findByMember(Member member);
    Optional<WishList> findByMemberAndProduct(Member member, Product product);
}
