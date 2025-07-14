package gift.repository;

import gift.entity.WishList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {
    private final JdbcTemplate jdbcTemplate;
    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public WishList save(WishList wishlist) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO wishlist(member_id, product_id, quantity) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, wishlist.getMemberId());
            ps.setLong(2, wishlist.getProductId());
            ps.setInt(3, wishlist.getQuantity());
            return ps;
        }, keyHolder);

        return new WishList(
                keyHolder.getKey().intValue(),
                wishlist.getMemberId(),
                wishlist.getProductId(),
                wishlist.getQuantity());
    }

    public Optional<WishList> findById(Integer id) {
        String sql = "SELECT * FROM wishlist WHERE id = ?";
        List<WishList> result = jdbcTemplate.query(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public List<WishList> findByMemberId(Integer memberId) {
        String sql = "SELECT * FROM wishlist WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), memberId);
    }

    public Optional<WishList> findByMemberIdAndProductId(Integer memberId, Integer productId) {
        String sql = "SELECT * FROM wishlist WHERE member_id = ? AND product_id = ?";
        List<WishList> result = jdbcTemplate.query(sql, (rs, rn) ->
                new WishList(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                ), memberId, productId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public int updateQuantity(Integer id, Integer quantity) {
        String sql = "UPDATE wishlist SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantity, id);
    }

    public int deleteById(Integer id) {
        String sql = "DELETE FROM wishlist WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
