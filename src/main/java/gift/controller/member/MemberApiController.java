package gift.controller.member;

import gift.domain.Member;
import gift.dto.jwt.TokenResponse;
import gift.dto.member.MemberRequest;
import gift.dto.IdResponse;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Valid @RequestBody MemberRequest memberRequest
    ){
        TokenResponse token = memberService.login(memberRequest);
        return ResponseEntity.status(HttpStatus.OK)
            .body(token);
    }

    // member 생성: 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> createMember(
        @Valid @RequestBody MemberRequest memberRequest
    ){
        Long memberId = memberService.insert(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new IdResponse(memberId));
    }

    // member 조회: 관리자용 기능
    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMember(
        @PathVariable Long memberId
    ){
        Member member = memberService.findById(memberId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(member);
    }


    // member 수정: 관리자용 기능
    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateMember(
        @Valid @RequestBody MemberRequest memberRequest,
        @PathVariable Long memberId
    ){
        memberService.update(memberId, memberRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // member 삭제: 관리자용 기능
    @PostMapping("/delete/{memberId}")
    public ResponseEntity<?> deleteMember(
        @PathVariable Long memberId
    ){
        memberService.deleteById(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
