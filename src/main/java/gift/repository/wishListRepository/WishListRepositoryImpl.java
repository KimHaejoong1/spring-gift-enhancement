package gift.repository.wishListRepository;

import gift.entity.WishItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishListRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WishItem addWishItem(Long userId, Long itemId, Integer quantity) {
        var insertSql = "INSERT INTO wish_items (user_id, item_id, quantity) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setLong(2, itemId);
            ps.setInt(3, quantity);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new WishItem(id, userId, itemId, quantity);
    }

    private final RowMapper<WishItem> wishItemRowMapper = (rs, rowNum) -> new WishItem(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getLong("item_id"),
            rs.getInt("quantity")
    );

    @Override
    public List<WishItem> getAllWishItems(Long userId) {
        var findSql = "SELECT id, user_id, item_id, quantity FROM wish_items WHERE user_id = ?";

        return jdbcTemplate.query(findSql, new Object[]{userId}, wishItemRowMapper);
    }

    @Override
    public WishItem updateWishItem(Integer quantity, Long itemId, Long userId) {
        var updateSql = "UPDATE wish_items SET quantity = ? WHERE user_id = ? AND item_id = ?";
        int updatedRows = jdbcTemplate.update(updateSql, quantity, userId, itemId);

        if (updatedRows == 0) {
            return null;
        }

        var selectSql = "SELECT id, user_id, item_id, quantity FROM wish_items WHERE user_id = ? AND item_id = ?";

        return jdbcTemplate.queryForObject(selectSql, new Object[]{userId, itemId}, wishItemRowMapper);
    }

    @Override
    public WishItem deleteWishItem(Long userId, Long itemId) {
        var selectSql = "SELECT id, user_id, item_id, quantity FROM wish_items WHERE user_id = ? AND item_id = ?";
        WishItem wishItem = jdbcTemplate.queryForObject(selectSql, new Object[]{userId, itemId}, wishItemRowMapper);

        var deleteSql = "DELETE FROM wish_items WHERE user_id = ? AND item_id = ?";
        jdbcTemplate.update(deleteSql, userId, itemId);

        return wishItem;
    }
}
