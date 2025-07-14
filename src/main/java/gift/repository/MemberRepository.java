package gift.repository;

import gift.entity.Member;
import gift.entity.Role;
import gift.exception.DataInsertFailedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role"))
            );
        }
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from members where email = ?";
        List<Member> members = jdbcTemplate.query(sql, new MemberRowMapper(), email);
        return Optional.ofNullable(members.isEmpty() ? null : members.getFirst());
    }

    public Member save(Member member) {
        String sql = "insert into members (name, email, password) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new DataInsertFailedException();
        }

        return new Member(key.longValue(), member.getName(), member.getEmail(), member.getPassword(),  member.getRole());
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from members where id = ?";

        List<Member> members = jdbcTemplate.query(sql, new MemberRowMapper(), id);
        return Optional.ofNullable(members.isEmpty() ? null : members.get(0));
    }

    public List<Member> findAll() {
        String sql = "select * from members";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    public Optional<Member> update(Long id, String name, String email, String password) {
        Optional<Member> member = findById(id);
        if (member.isEmpty()) {
            return Optional.empty();
        }
        String sql = "update members set name = ?, email = ?, password = ? where id = ?";
        jdbcTemplate.update(sql, name, email, password, id);

        return Optional.of(new Member(id, name, email, password));
    }

    public boolean deleteById(Long id) {
        Optional<Member> member = findById(id);
        if (member.isEmpty()) {
            return false;
        }

        String sql = "delete from members where id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
