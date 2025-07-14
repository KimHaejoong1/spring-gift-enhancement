package gift.repository;

import gift.entity.Product;
import gift.exception.DataInsertFailedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ProductRepository{

    private final JdbcTemplate jdbcTemplate;
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsById(Long productId) {
        String sql = "select count(*) from products where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count!=null && count>0;
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("imageUrl")
            );
        }
    }

    public Product save(Product product) {
        String sql = "INSERT INTO products (name, price, imageUrl) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new DataInsertFailedException();
        }

        return new Product(key.longValue(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Optional<Product> findById(Long productId) {
        String sql = "SELECT * FROM products WHERE id = ?";

        List<Product> products = jdbcTemplate.query(sql, new ProductRowMapper(), productId);
        return Optional.ofNullable(products.isEmpty() ? null : products.get(0));
    }

    public Optional<Product> update(Long id, String name, Integer price, String imageUrl) {

        Optional<Product> product = findById(id);
        if (product.isEmpty()) return Optional.empty();

        String sql = "UPDATE products SET name = ?, price = ?, imageUrl = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, price, imageUrl, id);

        return Optional.of(new Product(id, name, price, imageUrl));
    }

    public boolean deleteById(Long id) {
        Optional<Product> product = findById(id);
        if (product.isEmpty()) return false;

        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0 ;

    }

    public List<Product> findAll(int page, int size, String sortField, String sortDir) {

        List<String> sortFields = List.of("id", "name", "price");
        if (!sortFields.contains(sortField)) {
            sortField = "id";
        } // sortField 입력값이 없는 경우 기본값을 id로 설정

        String direction = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

        int safePage = Math.max(page, 1);
        int offset = (safePage - 1) * size;

        String sql = String.format(
                "SELECT * FROM products ORDER BY %s %s LIMIT ? OFFSET ?",
                sortField, direction
        );

        return jdbcTemplate.query(sql, new ProductRowMapper(), size, offset);
    }
}
