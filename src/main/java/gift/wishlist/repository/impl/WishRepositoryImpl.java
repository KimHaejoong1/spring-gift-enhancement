package gift.wishlist.repository.impl;

import gift.wishlist.model.Wish;
import gift.wishlist.repository.WishRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Wish> wishRowMapper = new RowMapper<Wish>() {
        @Override
        public Wish mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Wish(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getLong("quantity")
            );
        }
    };

    @Override
    public Wish save(Wish wish) {
        String sql = "INSERT INTO wishes (member_id, product_id, quantity) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, wish.getMemberId());
                ps.setLong(2, wish.getProductId());
                ps.setLong(3, wish.getQuantity());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            wish.setId(keyHolder.getKey().longValue());
        }

        return wish;
    }

    @Override
    public List<Wish> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wishes WHERE member_id = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, wishRowMapper, memberId);
    }
    @Override
    public Optional<Wish> findByProductId(Long productId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wishes WHERE product_id = ?";
        List<Wish> wishes = jdbcTemplate.query(sql, wishRowMapper, productId);
        return wishes.isEmpty() ? Optional.empty() : Optional.of(wishes.get(0));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM wishes WHERE id = ?";
        int deletedRows = jdbcTemplate.update(sql, id);

        if (deletedRows == 0) {
            throw new RuntimeException("위시리스트 항목을 찾을 수 없습니다. ID: " + id);
        }
    }

    @Override
    public boolean exists(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishes WHERE member_id = ? AND product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }


}
