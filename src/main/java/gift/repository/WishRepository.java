package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.entity.Wish;
import gift.exception.DataInsertFailedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class WishRowMapper implements RowMapper<Wish> {
        @Override
        public Wish mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Wish(
                    rs.getLong("id"),
                    rs.getLong("memberId"),
                    rs.getLong("productId")
            );
        }
    }

    public Optional<Wish> findById(Long id) {
        String sql = "select * from wishes where id = ?";
        List<Wish> wishes = jdbcTemplate.query(sql, new WishRowMapper(), id);
        return Optional.ofNullable(wishes.isEmpty() ? null : wishes.get(0));
    }

    public List<Wish> findByMemberId(Long memberId) {
        String sql = """
            SELECT w.id, w.memberId, w.productId
            FROM wishes w
            WHERE w.memberId = ?
        """;
        return jdbcTemplate.query(sql, new WishRowMapper(), memberId);
    }

    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishes WHERE memberId = ? AND productId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    public Wish save(Long memberId, Long productId) {
        String sql = "INSERT INTO wishes (memberId, productId) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, memberId);
            ps.setLong(2, productId);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new DataInsertFailedException();
        }
        return new Wish(key.longValue(), memberId, productId);

    }

    public boolean delete(Long wishId) {
        Optional<Wish> wish = findById(wishId);
        if (wish.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM wishes WHERE id = ?";
        return jdbcTemplate.update(sql, wishId) > 0;
    }
}