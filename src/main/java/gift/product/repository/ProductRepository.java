package gift.product.repository;

import gift.product.entity.Product;
import gift.exception.ProductNotFoundException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class ProductRepository {
    private final JdbcClient jdbcClient;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Product save(Product product){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("INSERT INTO product (name, price, image_url, is_kakao_approved) VALUES (:name, :price, :imageUrl, :isKakaoApprovedByMd)")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .param("isKakaoApprovedByMd", product.getIsKakaoApprovedByMd())
                .update(keyHolder, "id");

        Long newId = keyHolder.getKey().longValue();

        return new Product(newId, product.getName(), product.getPrice(), product.getImageUrl(), product.getIsKakaoApprovedByMd());
    }

    public Optional<Product> findById(Long id){
        return jdbcClient.sql("SELECT id, name, price, image_url, is_kakao_approved FROM product WHERE id = :id")
                .param("id", id)
                .query(getProductRowMapper())
                .optional();
    }

    public List<Product> findAll(){
        return jdbcClient.sql("SELECT id, name, price, image_url, is_kakao_approved FROM product")
                .query(getProductRowMapper())
                .list();
    }

    public List<Product> findAllByIdIn(List<Long> ids){
        String sql = "SELECT * FROM product WHERE id IN (:ids)";

        return jdbcClient.sql(sql)
                .param("ids", ids)
                .query(getProductRowMapper())
                .list();
    }

    public void deleteById(Long id){
        int affectedRows = jdbcClient.sql("DELETE FROM product WHERE id = :id")
                .param("id", id)
                .update();

        if (affectedRows == 0) {
            throw new ProductNotFoundException(id);
        }
    }


    public Product update(Product product) {
        int affectedRows = jdbcClient.sql("UPDATE product SET name = :name, price = :price, image_url = :imageUrl,is_kakao_approved = :isKakaoApprovedByMd  WHERE id = :id")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("imageUrl", product.getImageUrl())
                .param("id", product.getId())
                .param("isKakaoApprovedByMd", product.getIsKakaoApprovedByMd())
                .update();

        if (affectedRows == 0) {
            throw new ProductNotFoundException(product.getId());
        }

        return product;
    }

    private static RowMapper<Product> getProductRowMapper(){
        return (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("price"),
                rs.getString("image_url"),
                rs.getBoolean("is_kakao_approved")
        );
    }
}
