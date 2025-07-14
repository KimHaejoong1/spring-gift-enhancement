package gift.repository;

import gift.domain.Product;
import gift.dto.CreateProductRequest;
import gift.dto.UpdateProductRequest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public ProductJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Product save(CreateProductRequest request) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(request);
        Number key = jdbcInsert.executeAndReturnKey(param);
        Product product = new Product(key.longValue(), request.name(), request.price(), request.imageUrl());
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "select id, name, price, image_url from product where id = :id";
        return client.sql(sql)
                .param("id", id)
                .query(Product.class)
                .optional();
    }

    @Override
    public List<Product> findAll() {
        String sql = "select id, name, price, image_url from product";
        return client.sql(sql)
                .query(Product.class)
                .list();
    }

    @Override
    public Product update(Long id, UpdateProductRequest request) {
        String sql = "update product set name=:name, price=:price, image_url=:imageUrl where id = :id";
        client.sql(sql)
                .param("name", request.name())
                .param("price", request.price())
                .param("imageUrl", request.imageUrl())
                .param("id", id)
                .update();
        return new Product(id, request.name(), request.price(), request.imageUrl());
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from product where id = :id";
        client.sql(sql)
                .param("id", id)
                .update();
    }
//
//    private RowMapper<Product> productRowMapper() {
//        return (rs, rowNum) -> {
//            long id = rs.getLong("id");
//            String name = rs.getString("name");
//            int price = rs.getInt("price");
//            String imageUrl = rs.getString("image_url");
//
//            return new Product(id, name, price, imageUrl);
//        };
//    }



//    private RowMapper<Product> productRowMapper() {
//        return BeanPropertyRowMapper.newInstance(Product.class);
//    }
}
