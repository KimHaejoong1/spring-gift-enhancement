package gift.member.repository;


import gift.domain.Member;
import gift.domain.Role;
import gift.global.exception.CustomDatabaseException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static gift.util.UUIDParser.bytesToUUID;
import static gift.util.UUIDParser.uuidToBytes;

@Repository
public class MemberRepositoryV1 implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryV1(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (id, email, password, role) values (:id, :email, :password, :role)";

        int update = jdbcClient.sql(sql)
                .param("id", member.getId())
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("role", member.getRole().toString())
                .update();

        if (update == 0) throw new CustomDatabaseException("회원 저장 실패");

        return member;
    }

    @Override
    public List<Member> findAll() {

        String sql = "select * from member";

        return jdbcClient.sql(sql)
                .query(getMemberRowMapper())
                .list();
    }

    @Override
    public Optional<Member> findById(UUID id) {

        String sql = "select * from member where id = :id";

        return jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .query(getMemberRowMapper())
                .optional();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select * from member where email = :email";

        return jdbcClient.sql(sql)
                .param("email", email)
                .query(getMemberRowMapper())
                .optional();
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "delete from member where id = :id";

        int update = jdbcClient.sql(sql)
                .param("id", uuidToBytes(id))
                .update();

        if (update == 0) throw new CustomDatabaseException("회원 탈퇴 실패");
    }

    @Override
    public void deleteAll() {
        String sql = "delete from member";

        int update = jdbcClient.sql(sql)
                .update();
    }

    @Override
    public void update(Member member) {
        String sql = "update member set password = :password, role = :role where id = :id";

        int update = jdbcClient.sql(sql)
                .param("password", member.getPassword())
                .param("role", member.getRole().toString())
                .param("id", uuidToBytes(member.getId()))
                .update();

        if (update == 0) throw new CustomDatabaseException("회원 수정 실패");
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> {
            UUID id = bytesToUUID(rs.getBytes("id"));
            String email = rs.getString("email");
            String password = rs.getString("password");
            String role = rs.getString("role");
            return new Member(id, email, password, Role.valueOf(role));
        };
    }
}
