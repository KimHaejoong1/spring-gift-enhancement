package gift.repository.member;

import gift.entity.Member;
import java.util.Optional;

public interface MemberRepository {

    public Member create(Member member);

    public Optional<Member> findByEmail(String email);

    public int changePassword(Member member, String afterPassword);

    public boolean existsByEmail(String email);

    void resetPassword(Member member);

    public Optional<Member> findById(Long id);
}
