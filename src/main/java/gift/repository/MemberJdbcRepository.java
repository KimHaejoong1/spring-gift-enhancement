package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcClient client;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(DataSource dataSource) {
        this.client = JdbcClient.create(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(String email, String password, String salt) {
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password)
                .addValue("salt", salt);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return new Member(key.longValue(), email, password, salt);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select id, email, password, salt from member where id = :id";
        return client.sql(sql)
                .param("id", id)
                .query(Member.class)
                .optional();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select id, email, password, salt from member where email = :email";
        return client.sql(sql)
                .param("email", email)
                .query(Member.class)
                .optional();
    }

    @Override
    public List<Member> findAll() {
        String sql = "select id, email, password, salt from member";
        return client.sql(sql)
                .query(Member.class)
                .list();
    }

    @Override
    public Member update(Long id, String email, String password, String salt) {
        String sql = "update member set email = :newEmail, password = :password, salt = :salt where id = :id";
        client.sql(sql)
                .param("newEmail", email)
                .param("password", password)
                .param("salt", salt)
                .param("id", id)
                .update();
        return new Member(id, email, password, salt);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from member where id = :id";
        client.sql(sql)
                .param("id", id)
                .update();
    }
}
