package gift.member.service;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.LoginResponseDto;
import gift.member.dto.SignUpRequestDto;
import gift.member.dto.SignUpResponseDto;
import gift.member.model.Member;

public interface MemberService {
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    Member findById(Long memberId);
}
