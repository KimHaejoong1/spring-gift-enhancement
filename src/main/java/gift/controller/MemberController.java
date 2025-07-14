package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerMember(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.register(memberRequestDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) {
        return ResponseEntity.ok(
                authService.login(memberRequestDto)
        );
    }
}
