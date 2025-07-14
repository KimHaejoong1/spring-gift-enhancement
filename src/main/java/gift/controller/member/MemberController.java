package gift.controller.member;

import gift.dto.member.MemberPasswordChangeDto;
import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> create(@RequestBody MemberRequestDto requestDto) {
        return new ResponseEntity<>(memberService.create(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(
        @RequestBody MemberRequestDto requestDto
    ) {
        return new ResponseEntity<>(memberService.login(requestDto),
            HttpStatus.OK);
    }

    @PutMapping("/password/change")
    public ResponseEntity<Void> changePassword(
        @RequestBody MemberPasswordChangeDto requestDto
    ) {
        memberService.changePassword(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> restPassword(
        @RequestBody MemberRequestDto requestDto
    ) {
        memberService.resetPassword(requestDto);
        return ResponseEntity.ok().build();
    }
}
