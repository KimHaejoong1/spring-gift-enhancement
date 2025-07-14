package gift.repository;

import gift.entity.Wish;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryJDBCImpl implements WishRepository {

    private final JdbcClient jdbcClient;

    public WishRepositoryJDBCImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<Wish> ROW_MAPPER = (rs, rowNum) -> new Wish(
        rs.getLong("id"),
        rs.getLong("user_id"),
        rs.getLong("product_id"),
        rs.getInt("quantity")
    );

    @Override
    public Wish save(Wish wish) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                    INSERT INTO wish (user_id, product_id, quantity)
                    VALUES (:userId, :productId, :quantity)
                """)
            .param("userId", wish.getUserId())
            .param("productId", wish.getProductId())
            .param("quantity", wish.getQuantity())
            .update(keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return jdbcClient.sql("SELECT * FROM wish WHERE id = :id")
            .param("id", generatedId)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        Integer count = jdbcClient.sql("""
                    SELECT COUNT(*) FROM wish
                    WHERE user_id = :userId AND product_id = :productId
                """)
            .param("userId", userId)
            .param("productId", productId)
            .query(Integer.class)
            .single();

        return count != null && count > 0;
    }

    @Override
    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        jdbcClient.sql("""
                    DELETE FROM wish
                    WHERE user_id = :userId AND product_id = :productId
                """)
            .param("userId", userId)
            .param("productId", productId)
            .update();
    }

    @Override
    public List<Wish> findByUserId(Long userId) {
        return jdbcClient.sql("""
                    SELECT * FROM wish
                    WHERE user_id = :userId
                """)
            .param("userId", userId)
            .query(ROW_MAPPER)
            .list();
    }
}
