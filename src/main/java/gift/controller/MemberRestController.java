package gift.controller;

import gift.config.JwtTokenProvider;
import gift.dto.CreateMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberRequest;
import gift.dto.LoginMemberResponse;
import gift.service.MemberService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService service;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberRestController(MemberService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public HttpEntity<CreateMemberResponse> memberRegister(@Validated @RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = service.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public HttpEntity<LoginMemberResponse> memberLogin(@RequestBody LoginMemberRequest request) {
        service.login(request);
        String token = jwtTokenProvider.createToken(request.email());
        return new ResponseEntity<>(new LoginMemberResponse(token), HttpStatus.OK);
    }

}
