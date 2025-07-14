package gift.repository;

import gift.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO products(name, price, image_url) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setLong(2, product.getPrice().longValue());
            ps.setString(3, product.getImageUrl());
            return ps;
        });
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl());
        return product;
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";

        return jdbcTemplate.query(sql, (rs, rn) ->
                new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new BigInteger(rs.getString("price")),
                        rs.getString("image_url")
                )
        );
    }

    public Product findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rn) ->
                new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new BigInteger(rs.getString("price")),
                        rs.getString("image_url")
                ), id);
    }

    public int update(Integer id, Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                id);
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
