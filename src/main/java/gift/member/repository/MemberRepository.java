package gift.member.repository;


import gift.exception.MemberNotFoundByIdException;
import gift.member.entity.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Member save(Member member){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("INSERT INTO member (email, salt, password, role) VALUES (:email, :salt, :password, :role)")
                .param("email", member.getEmail())
                .param("salt", member.getSalt())
                .param("password", member.getPassword())
                .param("role", member.getRole())
                .update(keyHolder, "id");

        Long newId = keyHolder.getKey().longValue();

        return new Member(newId, member.getEmail(), member.getSalt(), member.getPassword(), member.getRole());
    }

    public Optional<Member> findById(Long id){
        return jdbcClient.sql("SELECT id, email, salt, password, role FROM member WHERE id = :id")
                .param("id", id)
                .query(getMemberRowMapper())
                .optional();
    }

    public Optional<Member> findByEmail(String email){
        return jdbcClient.sql("SELECT id, email, salt, password, role FROM member WHERE email = :email")
                .param("email", email)
                .query(getMemberRowMapper())
                .optional();
    }

    public List<Member> findAll(){
        return jdbcClient.sql("SELECT id, email, salt, password, role FROM member")
                .query(getMemberRowMapper())
                .list();
    }

    public void deleteById(Long id){
        int affectedRows = jdbcClient.sql("DELETE FROM member WHERE id = :id")
                .param("id", id)
                .update();

        if (affectedRows == 0) {
            throw new MemberNotFoundByIdException(id);
        }
    }

    public Member update(Member member) {
        int affectedRows = jdbcClient.sql("UPDATE member SET email = :email, salt = :salt, password = :password, role = :role WHERE id = :id")
                .param("id", member.getId())
                .param("email", member.getEmail())
                .param("salt", member.getSalt())
                .param("password", member.getPassword())
                .param("role", member.getRole())
                .update();

        if (affectedRows == 0) {
            throw new MemberNotFoundByIdException(member.getId());
        }

        return member;
    }

    private static RowMapper<Member> getMemberRowMapper(){
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("salt"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
