package gift.api.member.repository;

import gift.api.member.domain.Member;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return jdbcClient.sql("select * from member where email = :email")
                .param("email", email)
                .query(Member.class)
                .optional();
    }

    @Override
    public Member registerMember(Member member) {
        jdbcClient.sql(
                        "insert into member (email, password, role) values (:email, :password, :role)")
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("role", member.getRole().name())
                .update();

        return findByEmail(member.getEmail()).get();
    }
}
