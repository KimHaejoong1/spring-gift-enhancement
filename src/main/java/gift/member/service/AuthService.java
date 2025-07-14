package gift.member.service;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.TokenResponseDto;

public interface AuthService {

    TokenResponseDto signUp(MemberRequestDto requestDto);
    TokenResponseDto login(MemberRequestDto requestDto);
}
