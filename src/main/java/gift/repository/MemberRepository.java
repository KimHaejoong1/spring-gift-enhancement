package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert memberInserter;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    // 회원 저장
    public Member save(Member member) {
        Map<String, Object> params = Map.of(
                "email", member.getEmail(),
                "password", member.getPassword()
        );

        Number id = memberInserter.executeAndReturnKey(new MapSqlParameterSource(params));
        return Member.withId(id.longValue(), member.getEmail(), member.getPassword());
    }

    // 이메일로 회원 찾기
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<Member> result = jdbcTemplate.query(sql, rowMapper(), email);
        return result.stream().findAny();
    }

    // ID로 회원 찾기
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> result = jdbcTemplate.query(sql, rowMapper(), id);
        return result.stream().findAny();
    }

    private RowMapper<Member> rowMapper() {
        return (rs, rowNum) -> Member.withId(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}