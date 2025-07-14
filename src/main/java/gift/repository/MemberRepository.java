package gift.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import gift.domain.Member;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String SAVE_MEMBER = """
            INSERT INTO members(email, passwordHash) VALUES
            (:email, :passwordHash);
            """;
    private static final String FIND_MEMBER_BY_EMAIL = """
            SELECT id, email, passwordHash
            FROM members
            WHERE email = :email;
            """;
    private static final String FIND_MEMBER_BY_ID = """
            SELECT id, email, passwordHash
            FROM members
            WHERE id = :id;
            """;

    public Optional<Number> saveMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(SAVE_MEMBER)
                .param("email", member.getEmail())
                .param("passwordHash", member.getPasswordHash())
                .update(keyHolder);
        return Optional.ofNullable(keyHolder.getKey());
    }

    public Optional<Member> findMemberByEmail(String email) {
        return jdbcClient.sql(FIND_MEMBER_BY_EMAIL)
                .param("email", email)
                .query((rs, numOfRows) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("passwordHash")
                        )).optional();
    }

    public Optional<Member> findMemberById(long id) {
        return jdbcClient.sql(FIND_MEMBER_BY_ID)
                .param("id", id)
                .query((rs, numOfRows) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("passwordHash")
                        )).optional();
    }
}
