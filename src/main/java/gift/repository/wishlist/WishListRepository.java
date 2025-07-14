package gift.repository.wishlist;

import gift.domain.WishList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<WishList> rowMapper = (rs, rowNum) -> new WishList(
        rs.getLong("id"),
        rs.getLong("member_id"),
        rs.getLong("product_id"),
        rs.getInt("quantity")
    );
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public WishListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 조회
    public List<WishList> findWishListAllById(Long memberId) {
        String sql = "select * from wishlists where member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public WishList findWishListByMemberAndProduct(Long memberId, Long productId) {
        String sql = "select * from wishlists where member_id = ? and product_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, memberId, productId);
    }

    // 생성
    public Long insertWishList(Long memberId, Long productId, int quantity) {
        String sql = "insert into wishlists(member_id, product_id, quantity) values(?, ?, ?)";

        jdbcTemplate.update((Connection con) -> {
            PreparedStatement st = con.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS);
            st.setLong(1, memberId);
            st.setLong(2, productId);
            st.setInt(3, quantity);

            return st;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    // 수정
    public void updateWishList(Long wishListId, int quantity) {
        String sql = "update wishlists set quantity = ? where id =?";
        jdbcTemplate.update(sql, quantity, wishListId);
    }

    // 삭제
    public void deleteById(Long wishListId) {
        String sql = "delete from wishlists where id =?";
        jdbcTemplate.update(sql, wishListId);
    }
}
