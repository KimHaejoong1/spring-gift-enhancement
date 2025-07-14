package gift.service.member;

import gift.dto.member.MemberPasswordChangeDto;
import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.dto.member.MemberCredentialDto;

public interface MemberService {

    public MemberResponseDto create(MemberRequestDto requestDto);

    public MemberResponseDto login(MemberRequestDto requestDto);

    void changePassword(MemberPasswordChangeDto requestDto);

    void resetPassword(MemberRequestDto requestDto);

    MemberCredentialDto findById(Long id);
}