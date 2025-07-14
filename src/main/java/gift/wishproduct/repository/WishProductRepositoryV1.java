package gift.wishproduct.repository;

import gift.domain.WishProduct;
import gift.global.exception.CustomDatabaseException;
import gift.wishproduct.dto.WishProductResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static gift.util.UUIDParser.bytesToUUID;
import static gift.util.UUIDParser.uuidToBytes;

@Repository
public class WishProductRepositoryV1 implements WishProductRepository {

    private final JdbcClient jdbcClient;

    public WishProductRepositoryV1(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public WishProduct save(WishProduct wishProduct) {
        String sql = "insert into wish_product " +
                "values(:id, :quantity, :owner_id, :product_id)";

        int update = jdbcClient.sql(sql)
                .param("id", wishProduct.getId())
                .param("quantity", wishProduct.getQuantity())
                .param("owner_id", uuidToBytes(wishProduct.getOwnerId()))
                .param("product_id", uuidToBytes(wishProduct.getProductId()))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 상품 저장 실패");

        return wishProduct;
    }

    @Override
    public Optional<WishProduct> findById(UUID id) {

        String sql = "select * from wish_product where id = :id";

        return jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .query(getWishProductRowMapper())
                .optional();
    }

    @Override
    public List<WishProduct> findByOwnerId(UUID ownerId) {

        String sql = "select * from wish_product where owner_id = :owner_id";

        return jdbcClient.sql(sql)
                .param("owner_id", uuidToBytes(ownerId))
                .query(getWishProductRowMapper())
                .list();
    }

    @Override
    public List<WishProduct> findByProductId(UUID productId) {
        String sql = "select * from wish_product where product_id = :product_id";

        return jdbcClient.sql(sql)
                .param("product_id", uuidToBytes(productId))
                .query(getWishProductRowMapper())
                .list();
    }

    public List<WishProductResponse> findWithProductByOwnerId(UUID ownerId) {
        String sql = "select * from wish_product w " +
                "join product p on w.product_id = w.product_id where w.owner_id = :owner_id";

        return jdbcClient.sql(sql)
                .param("owner_id", uuidToBytes(ownerId))
                .query(getWishProductResponseRowMapper())
                .list();
    }

    @Override
    public Optional<WishProduct> findByOwnerIdAndProductId(UUID ownerId, UUID productId) {
        String sql = "select * from wish_product where owner_id = :owner_id and product_id = :product_id";

        return jdbcClient.sql(sql)
                .param("owner_id", uuidToBytes(ownerId))
                .param("product_id", uuidToBytes(productId))
                .query(getWishProductRowMapper())
                .optional();
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "delete from wish_product where id = :id";

        int update = jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 상품 제거 실패");
    }

    @Override
    public void update(WishProduct wishProduct) {
        String sql = "update wish_product set quantity = :quantity where id = :id";

        int update = jdbcClient.sql(sql)
                .param("quantity", wishProduct.getQuantity())
                .param("id", uuidToBytes(wishProduct.getId()))
                .update();

        if (update == 0) throw new CustomDatabaseException("위시 리스트 수정 실패");
    }

    @Override
    public void deleteAll() {
        String sql = "delete from wish_product";

        jdbcClient.sql(sql)
                .update();
    }

    private RowMapper<WishProduct> getWishProductRowMapper() {
        return (rs, rowNum) -> {
            UUID id = bytesToUUID(rs.getBytes("id"));
            int quantity = rs.getInt("quantity");
            UUID ownerId = bytesToUUID(rs.getBytes("owner_id"));
            UUID productId = bytesToUUID(rs.getBytes("product_id"));
            return new WishProduct(id, quantity, ownerId, productId);
        };
    }

    private RowMapper<WishProductResponse> getWishProductResponseRowMapper() {
        return (rs, rowNum)-> {
            UUID id = bytesToUUID(rs.getBytes("id"));
            String name = rs.getString("name");
            int quantity = rs.getInt("quantity");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            UUID ownerId = bytesToUUID(rs.getBytes("owner_id"));
            UUID productId = bytesToUUID(rs.getBytes("product_id"));
            return new WishProductResponse(id, name, price, quantity, imageUrl);
        };
    }
}
