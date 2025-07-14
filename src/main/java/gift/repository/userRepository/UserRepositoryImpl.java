package gift.repository.userRepository;


import gift.entity.User;
import gift.entity.UserRole;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"), UserRole.valueOf(rs.getString("role")));
        }
    };

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(String email, String password, UserRole role) {
        var sql = "INSERT INTO users (email,password,role) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role.name());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new User(id, email, password, role);

    }

    @Override
    public List<User> getAllUsers() {
        var sql = "SELECT id, email, password, role FROM users";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public List<User> findUsersByEmail(String email) {
        var sql = "SELECT id, email, password, role FROM users WHERE EMAIL = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, userRowMapper);

    }

    @Override
    public User findUserById(Long id) {
        var sql = "SELECT id, email, password, role FROM users WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, userRowMapper);
    }

    @Override
    public User updateUser(User findUser, String email, String password) {
        var sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, email, password, findUser.id());
        return new User(findUser.id(), email, password,findUser.role());
    }

    @Override
    public void deleteUser(User findUser) {
        var sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, findUser.id());
    }


    @Override
    public User findUserByEmail(String email) {
        var sql = "SELECT id, email, password, role FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
