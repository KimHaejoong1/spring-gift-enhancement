package gift.repository.wishlist;

import gift.entity.Wish;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class WishListRepositoryImpl implements WishListRepository {

    private final JdbcClient jdbcClient;
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Wish> WISH_ROW_MAPPER = ((rs, rowNum) -> new Wish(
        rs.getLong("id"), rs.getLong("product_id"), rs.getLong("member_id")));

    public WishListRepositoryImpl(JdbcClient jdbcClient, JdbcTemplate jdbcTemplate) {
        this.jdbcClient = jdbcClient;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Transactional
    public Wish create(Wish wish) {
        String sql = "insert into wishlist(product_id, member_id) values (:productId, :memberId)";

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("wishlist").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("product_id", wish.getProductId());
        params.put("member_id", wish.getMemberId());

        Long id = (Long) jdbcInsert.executeAndReturnKey(params);

        return new Wish(id, wish.getProductId(), wish.getMemberId());
    }

    @Override
    public List<Wish> findAll(Long memberId) {
        String sql = "select id, product_id, member_id from wishlist where member_id = :memberId";

        List<Wish> wishList = jdbcClient.sql(sql)
            .param("memberId", memberId)
            .query(WISH_ROW_MAPPER)
            .list();

        return wishList;
    }

    @Override
    @Transactional
    public int delete(Long productId, Long memberId) {
        String sql = "delete from wishlist where product_id = :productId and member_id = :memberId";

        int deleteRow = jdbcClient.sql(sql)
            .param("productId", productId)
            .param("memberId", memberId)
            .update();

        return deleteRow;
    }
}
