package gift.repository;

import gift.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(String email, String password, String salt);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    Member update(Long id, String email, String password, String salt);

    void delete(Long id);
}
