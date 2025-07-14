package gift.member.service;

import gift.domain.Member;
import gift.member.dto.MemberCreateDto;
import gift.member.dto.MemberResponse;
import gift.member.dto.MemberUpdateReqForAdmin;
import gift.member.dto.MemberUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    UUID save(MemberCreateDto memberCreateDto);

    void changePassword(String email, MemberUpdateRequest memberUpdateRequest);

    void updateMemberForAdmin(UUID id, MemberUpdateReqForAdmin memberUpdateReqForAdmin);

    MemberResponse findById(UUID id);

    List<MemberResponse> findAll();

    void deleteByEmail(String email);

    void deleteById(UUID id);

    void validateToken(String email, String role);

    MemberResponse validate(String email, String password);

    Member findByEmail(String email);
}
