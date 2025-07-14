package gift.repository;

import gift.entity.User;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryJDBCImpl implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryJDBCImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password")
    );

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                    INSERT INTO users (email, password)
                    VALUES (:email, :password)
                """)
            .param("email", user.getEmail())
            .param("password", user.getPassword())
            .update(keyHolder);

        return jdbcClient.sql("SELECT * FROM users WHERE email = :email")
            .param("email", user.getEmail())
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM users WHERE email = :email")
            .param("email", email)
            .query(ROW_MAPPER)
            .optional();
    }
}
