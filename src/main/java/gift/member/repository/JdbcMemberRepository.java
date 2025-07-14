package gift.member.repository;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.member.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveMember(Member member) {
        String sql = "INSERT INTO member (email, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getRole().name());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, memberRowMapper())
                .stream().findFirst();
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberRowMapper())
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, memberRowMapper())
                .stream().findFirst();
    }

    @Override
    public void updateMember(Long id, MemberRequestDto requestDto) {
        String sql = "UPDATE member SET email = ?, password = ?, role = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.email(), requestDto.password(), requestDto.role().name(), id);
    }

    @Override
    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
