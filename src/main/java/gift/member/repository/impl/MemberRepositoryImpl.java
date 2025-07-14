package gift.member.repository.impl;

import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = new RowMapper<Member>() {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        }
    };

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO members (email, password, role) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, member.getEmail());
                ps.setString(2, member.getPassword());
                ps.setString(3, member.getRole());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            member.setId(keyHolder.getKey().longValue());
        }

        return member;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, role FROM members WHERE email = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, email);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password, role FROM members WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, id);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }
}
