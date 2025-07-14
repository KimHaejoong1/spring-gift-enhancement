package gift.api.wishlist.repository;

import gift.api.wishlist.domain.Wishlist;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistJdbcRepository implements WishlistRepository {

    private final JdbcClient jdbcClient;

    private static final Map<String, String> ALLOWED_SORT_CRITERIA = Map.of(
            "id", "id",
            "createdDate", "created_date",
            "productId", "product_id"
    );

    public WishlistJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Page<Wishlist> findWishlistByMemberId(Long memberId, Pageable pageable) {
        String sortOrder = pageable.getSort().stream()
                .filter(order -> ALLOWED_SORT_CRITERIA.containsKey(order.getProperty()))
                .map(order -> ALLOWED_SORT_CRITERIA.get(order.getProperty())
                        + " " + order.getDirection())
                .collect(Collectors.joining(", "));

        if (sortOrder.isEmpty()) {
            sortOrder = "created_date DESC";
        }

        String sql = "SELECT * FROM wishlist WHERE member_id = :memberId ORDER BY " + sortOrder
                + " LIMIT :limit OFFSET :offset";

        List<Wishlist> wishlist = jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("limit", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(Wishlist.class)
                .list();

        String countSql = "select count(*) from wishlist where member_id = :memberId";

        Long total = jdbcClient.sql(countSql)
                .param("memberId", memberId)
                .query(Long.class)
                .single();

        return new PageImpl<>(wishlist, pageable, total);
    }

    @Override
    public Optional<Wishlist> findWishlistByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "select * from wishlist where member_id = :memberId and product_id = :productId";

        Optional<Wishlist> wishlist = jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .query(Wishlist.class)
                .optional();

        return wishlist;
    }

    @Override
    public Optional<Wishlist> findById(Long id) {
        String sql = "select * from wishlist where id = :id";

        Optional<Wishlist> wishlist = jdbcClient.sql(sql)
                .param("id", id)
                .query(Wishlist.class)
                .optional();

        return wishlist;
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into wishlist (member_id, product_id, created_date) values (:memberId, :productId, :createdDate)";

        jdbcClient.sql(sql)
                .param("memberId", wishlist.getMemberId())
                .param("productId", wishlist.getProductId())
                .param("createdDate", wishlist.getCreatedDate())
                .update(keyHolder, "id");

        Long newId = keyHolder.getKey().longValue();

        return new Wishlist(newId, wishlist.getMemberId(), wishlist.getProductId(),
                wishlist.getCreatedDate());
    }

    @Override
    public boolean deleteWishlist(Long id, Long memberId) {
        String sql = "delete from wishlist where id = :id and member_id = :memberId";

        int deleted = jdbcClient.sql(sql)
                .param("id", id)
                .param("memberId", memberId)
                .update();

        return deleted > 0;
    }
}
