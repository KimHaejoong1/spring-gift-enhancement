package gift.member.controller;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final AuthenticationService authService;

    public MemberApiController(AuthenticationService authMemberService) {
        this.authService = authMemberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> signUp(@RequestBody @Valid MemberRequestDto requestDto) {
        TokenResponseDto responseDto = authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid MemberRequestDto requestDto) {
        TokenResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
