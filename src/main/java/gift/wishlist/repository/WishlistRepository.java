package gift.wishlist.repository;


import gift.exception.WishNotFoundByMemberIdAndProductId;
import gift.exception.WishNotFoundByMemberIdAndWishId;
import gift.wishlist.entity.Wishlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {
    private static final Logger log = LoggerFactory.getLogger(WishlistRepository.class);
    private final JdbcClient jdbcClient;

    public WishlistRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Wishlist saveWish(Long memberId, Long productId, int quantity) {
        String sql = """
                MERGE INTO wishlist
                USING dual
                ON (member_id = :memberId AND product_id = :productId)
                WHEN MATCHED THEN 
                    UPDATE SET quantity = quantity + :quantity
                WHEN NOT MATCHED THEN 
                    INSERT (member_id, product_id, quantity)
                    VALUES (:memberId, :productId, :quantity)
        """;

        jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("quantity", quantity)
                .update();

        return findWishByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new WishNotFoundByMemberIdAndProductId(memberId, productId));

    }

    public Optional<Wishlist> findWishByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = """
                SELECT id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = :memberId
                AND product_id = :productId
                """;
        return jdbcClient.sql(sql)
                .param("memberId", memberId)
                .param("productId",productId)
                .query(getRowMapper())
                .optional();
    }

    public List<Wishlist> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT id, member_id, product_id, quantity
                FROM wishlist
                WHERE member_id = :memberId
                """;
        return jdbcClient.sql(sql)
                .param("memberId",memberId)
                .query(getRowMapper())
                .list();
    }

    public void deleteWishByMemberIdAndWishId(Long memberId, Long wishId) {
        String sql = """
                DELETE FROM wishlist
                WHERE id = :wishId
                AND member_id = :memberId
                """;

        int affectedRows = jdbcClient.sql(sql)
                .param("wishId",wishId)
                .param("memberId",memberId)
                .update();

        if(affectedRows == 0){
            throw new WishNotFoundByMemberIdAndWishId(memberId, wishId);
        }
    }

    private static RowMapper<Wishlist> getRowMapper(){
        return (rs, rowNum) -> new Wishlist(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity")
        );
    }
}
