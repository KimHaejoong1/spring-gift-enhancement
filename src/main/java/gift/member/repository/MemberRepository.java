package gift.member.repository;

import gift.domain.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {

    Member save(Member member);

    List<Member> findAll();

    Optional<Member> findById(UUID id);

    Optional<Member> findByEmail(String email);

    void deleteById(UUID id);

    void deleteAll();

    void update(Member member);
}
