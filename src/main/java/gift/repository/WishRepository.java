package gift.repository;

import gift.domain.Product;
import gift.domain.Wish;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepository {

    private static final String GET_WISH_PRODUCTS_BY_MEMBER_ID = """
            SELECT products.id as id, products.name as name, products.price as price, products.imageUrl as imageUrl, products.status as status, count
            FROM products JOIN
            (wishes JOIN members ON wishes.memberId = members.id) as w
            ON products.id = w.productId
            WHERE w.memberId = :memberId;
            """;

    private static final String CREATE_WISH = """
            INSERT INTO wishes(memberId, productId, count) VALUES
            (:memberId, :productId, :count);
            """;

    private static final String UPDATE_WISH_COUNT = """
            UPDATE wishes SET count = :count
            WHERE memberId = :memberId and productId = :productId;
            """;

    private static final String DELETE_WISH = """
            DELETE FROM wishes
            WHERE memberId = :memberId and productId = :productId;
            """;

    private final JdbcClient jdbcClient;

    public WishRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void updateWishCount(long memberId, long productId, int count) {
        int numOfUpdatedRows = jdbcClient.sql(UPDATE_WISH_COUNT)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("count", count)
                .update();
        if (numOfUpdatedRows == 0)
            throw new IllegalArgumentException("not found wish");
    }

    public void saveWish(long memberId, long productId, int count) {
        int numOfUpdatedRows = jdbcClient.sql(CREATE_WISH)
                .param("memberId", memberId)
                .param("productId", productId)
                .param("count", count)
                .update();
        if (numOfUpdatedRows == 0)
            throw new IllegalArgumentException("not created");
    }

    public void deleteWish(long memberId, long productId) {
        jdbcClient.sql(DELETE_WISH)
                .param("memberId", memberId)
                .param("productId", productId)
                .update();
    }

    public List<Wish> getWishProductsByMemberId(long memberId) {
        return jdbcClient.sql(GET_WISH_PRODUCTS_BY_MEMBER_ID)
                .param("memberId", memberId)
                .query((rs, numOfRows) ->
                    new Wish(
                        rs.getInt("count"),
                        new Product(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getInt("price"),
                                rs.getString("imageUrl"),
                                Product.Status.valueOf(rs.getString("status"))
                        )
                    )
                ).list();
    }
}
