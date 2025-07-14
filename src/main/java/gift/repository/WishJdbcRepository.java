package gift.repository;

import gift.domain.Wish;
import gift.dto.WishResponse;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class WishJdbcRepository implements WishRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public WishJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("wish")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Wish save(Long productId, Long memberId, int quantity) {
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("product_id", productId)
                .addValue("member_id", memberId)
                .addValue("quantity", quantity);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return new Wish(key.longValue(), memberId, productId, quantity);
    }

    @Override
    public Optional<Wish> findById(Long id) {
        String sql = "select * from wish where id = :id";
        return client.sql(sql)
                .param("id", id)
                .query(Wish.class)
                .optional();
    }

    @Override
    public List<WishResponse> findAllByMember(Long memberId) {
        String sql = "select wish.id, product.name as productName, product.price, wish.quantity " +
                "from wish " +
                "join product on wish.product_id = product.id " +
                "join member on wish.member_id = member.id " +
                "where member.id = :id";
        return client.sql(sql)
                .param("id", memberId)
                .query(WishResponse.class)
                .list();
    }

    @Override
    public void update(int quantity, Long wishId) {
        String sql = "update wish set quantity = :quantity where id = :id";
        client.sql(sql)
                .param("quantity", quantity)
                .param("id", wishId)
                .update();
    }

    @Override
    public void delete(Long wishId) {
        String sql = "delete from wish where id = :wishId";
        client.sql(sql)
                .param("wishId", wishId)
                .update();
    }
}
