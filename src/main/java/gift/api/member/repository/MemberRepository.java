package gift.api.member.repository;

import gift.api.member.domain.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member registerMember(Member member);
}
