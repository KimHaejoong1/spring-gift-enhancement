package gift.api.product.repository;

import gift.api.product.domain.Product;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final JdbcClient jdbcClient;

    private static final Map<String, String> ALLOWED_SORT_CRITERIA = Map.of(
            "id", "id",
            "name", "name",
            "price", "price"
    );

    public ProductJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable, Long categoryId) {
        String where = categoryId != null ? " where category_id = :category_id" : "";

        String sortOrder = pageable.getSort().stream()
                .filter(order -> ALLOWED_SORT_CRITERIA.containsKey(order.getProperty()))
                .map(order -> ALLOWED_SORT_CRITERIA.get(order.getProperty())
                        + " " + order.getDirection())
                .collect(Collectors.joining(", "));

        if (sortOrder.isEmpty()) {
            sortOrder = "id ASC";
        }

        String paging = " limit :limit offset :offset";

        var query = jdbcClient.sql(
                        "select * from product" + where + " order by " + sortOrder + paging)
                .param("limit", pageable.getPageSize())
                .param("offset", pageable.getOffset());

        var count = jdbcClient.sql("select count(*) from product" + where);

        if (categoryId != null) {
            query = query.param("category_id", categoryId);
            count = count.param("category_id", categoryId);
        }

        return new PageImpl<>(
                query.query(Product.class).list(),
                pageable,
                count.query(Long.class).single()
        );
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return jdbcClient.sql("select * from product where id = :id")
                .param("id", id)
                .query(Product.class)
                .optional();
    }

    @Override
    public Product createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(
                        "insert into product (name, price, image_url) values (:name, :price, :image_url)")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .update(keyHolder, "id");

        Long newId = keyHolder.getKey().longValue();

        return findProductById(newId).get();
    }

    @Override
    public Product updateProduct(Product product) {
        jdbcClient.sql(
                        "update product set name = :name, price = :price, image_url = :image_url where id = :id")
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .param("id", product.getId())
                .update();

        return findProductById(product.getId()).get();
    }

    @Override
    public boolean deleteProduct(Long id) {
        int deleted = jdbcClient.sql("delete from product where id = :id")
                .param("id", id)
                .update();

        return deleted > 0;
    }
}