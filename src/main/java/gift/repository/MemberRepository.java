package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM members";

        return jdbcTemplate.query(sql, (rs, rn) ->
                new Member(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                )
        );
    }

    public Member findById(Integer id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rn) ->
                new Member(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                ), id);
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from members where email = ?";
        List<Member> members = jdbcTemplate.query(sql, (rs, rn) ->
                new Member(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                ), email);

        return members.isEmpty() ? Optional.empty() : Optional.of(members.getFirst());
    }

    public Member register(Member member) {
        String sql = "insert into members (email, password, role) values (?, ?, ?)";

        jdbcTemplate.update(sql,
                member.getEmail(),
                member.getPassword(),
                member.getRole().name());

        return new Member(0, member.getEmail(), member.getPassword(), member.getRole());
    }

    public int update(Integer id, Member member) {
        String sql = "update members set email = ?, password = ?, role = ? where id = ?";
        return jdbcTemplate.update(sql,
                member.getEmail(),
                member.getPassword(),
                member.getRole().name(),
                id);
    }

    public int delete(Integer id) {
        String sql = "delete from members where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
