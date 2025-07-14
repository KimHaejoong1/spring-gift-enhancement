package gift.member.controller;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import gift.exception.GlobalExceptionHandler.ApiResponse;


@Controller
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //회원 가입 기능
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MemberResponseDto>> register(@Valid @RequestBody MemberRequestDto memberRequestDto){

        MemberResponseDto memberResponseDto = memberService.register(memberRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(200 , "회원가입 성공" , memberResponseDto));
    }

    //로그인 기능
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponseDto>> login(@Valid @RequestBody MemberRequestDto memberRequestDto){

        MemberResponseDto memberResponseDto = memberService.login(memberRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(200 , "로그인 성공" , memberResponseDto));
    }
}
