package gift.repository.itemRepository;

import gift.entity.Item;
import gift.repository.userRepository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


@Repository
public class ItemRepositoryJdbcTemplate implements ItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Item> itemRowMapper = new RowMapper<>() {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Item(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );
        }
    };
    private final UserRepository userRepository;

    public ItemRepositoryJdbcTemplate(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public Item saveItem(Item item) {
        var sql = "insert into items (name, price, image_url) values (?, ?, ?)";
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, item.getName());
            ps.setInt(2, item.getPrice());
            ps.setString(3, item.getImageUrl());
            return ps;
        }, keyholder);

        Long id = keyholder.getKey().longValue();
        return new Item(id, item.getName(), item.getPrice(), item.getImageUrl());

    }

    @Override
    public List<Item> getItems(String name, Integer price) {
        var sql = "select id, name, price, image_url from items where name=? or price=?";
        return jdbcTemplate.query(
                sql,
                new Object[]{name, price},
                itemRowMapper
        );

    }

    @Override
    public Item deleteItems(String name) {
        var sql = "SELECT id, name, price, image_url FROM items WHERE name = ? LIMIT 1";
        Item item = jdbcTemplate.queryForObject(sql, new Object[]{name}, itemRowMapper);


        String DeleteSql = "DELETE FROM items WHERE id = ?";
        jdbcTemplate.update(DeleteSql, item.getId());

        return item;
    }

    @Override
    public Item findById(Long id) {
        String sql = "SELECT id, name, price, image_url FROM items WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, itemRowMapper);
    }

    @Override
    public List<Item> getAllItems() {
        var sql = "select id, name, price, image_url from items";

        return jdbcTemplate.query(sql, itemRowMapper);
    }

    @Override
    public Item deleteById(Long id) {
        var sql = "SELECT id, name, price, image_url FROM items WHERE id = ?";
        Item item = jdbcTemplate.queryForObject(sql, new Object[]{id}, itemRowMapper);


        var deleteSql = "DELETE FROM items WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);

        return item;
    }

    @Override
    public Item updateItem(Long id, String name, int price, String imageUrl) {
        var sql = "UPDATE items SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, price, imageUrl, id);

        return findById(id);
    }

    @Override
    public Item findItemByName(String name) {
        var sql = "SELECT id, name, price, image_url FROM items WHERE name = ?";
        Item item = jdbcTemplate.queryForObject(sql, new Object[]{name}, itemRowMapper);

        return item;
    }

    @Override
    public Item findItemById(Long itemId) {
        var sql = "SELECT id, name, price, image_url FROM items WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{itemId}, itemRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
