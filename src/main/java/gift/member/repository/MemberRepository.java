package gift.member.repository;

import gift.member.dto.MemberRequestDto;
import gift.member.entity.Member;
import gift.product.dto.ProductResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> {
        Member member = new Member(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );

        return member;
    };

    //멤버 추가
    public Member registerMember(MemberRequestDto memberRequestDto){

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into member (email, password, role) values (?, ?, ?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, memberRequestDto.getEmail());
                ps.setString(2, memberRequestDto.getPassword());
                ps.setString(3, memberRequestDto.getRole());
                return ps;
            }
        },keyHolder);

        if(keyHolder.getKey() != null){
            return new Member(
                    keyHolder.getKey().longValue(),
                    memberRequestDto.getEmail(),
                    memberRequestDto.getPassword(),
                    memberRequestDto.getRole());
        }

        return null;
    }

    //멤버 조회
    public Optional<Member> findMember(MemberRequestDto memberRequestDto) {

        String sql = "select * from member where email = ? and password = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                memberRowMapper,
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword()));

    }

    public Optional<Member> findMemberById(Long memberId) {
        String sql = "select * from member where id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql,memberRowMapper,memberId)
        );
    }

}
