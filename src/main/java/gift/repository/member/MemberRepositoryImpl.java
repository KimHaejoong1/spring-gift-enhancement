package gift.repository.member;

import gift.entity.Member;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    private final static RowMapper<Member> MEMBER_ROW_MAPPER = ((rs, rowNum) -> new Member(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password")
    ));

    @Autowired
    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Member create(Member member) {
        String sql = "INSERT INTO member(email, password) VALUES(:email, :password)";

        jdbcClient.sql(sql)
            .param("email", member.getEmail())
            .param("password", member.getPassword())
            .update();

        return member;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(MEMBER_ROW_MAPPER)
            .optional();
    }

    @Override
    public int changePassword(Member member, String afterPassword) {
        String sql = "UPDATE member SET password = ? WHERE email = ? and password = ?";

        int changeRow = jdbcClient.sql(sql)
            .param(afterPassword)
            .param(member.getEmail())
            .param(member.getPassword())
            .update();

        return changeRow;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT count(*) FROM member WHERE email = :email";

        int findCount = jdbcClient.sql(sql)
            .param("email", email)
            .query(Integer.class)
            .single();

        return findCount > 0;
    }

    @Override
    public void resetPassword(Member member) {
        String sql = "UPDATE member SET password = :password WHERE email = :email";

        jdbcClient.sql(sql)
            .param("password", member.getPassword())
            .param("email", member.getEmail())
            .update();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(MEMBER_ROW_MAPPER)
            .optional();
    }
}
