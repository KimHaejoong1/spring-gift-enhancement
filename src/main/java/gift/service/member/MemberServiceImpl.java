package gift.service.member;

import gift.dto.member.MemberPasswordChangeDto;
import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.dto.member.MemberCredentialDto;
import gift.entity.Member;
import gift.repository.member.MemberRepository;
import gift.util.JwtUtil;
import gift.util.Sha256Util;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final Sha256Util sha256Util;

    public MemberServiceImpl(MemberRepository memberRepository, JwtUtil jwtUtil,
        Sha256Util sha256Util) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.sha256Util = sha256Util;
    }

    @Override
    public MemberResponseDto create(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Member member = memberRepository.create(
            new Member(requestDto.email(), sha256Util.encrypt(requestDto.password())));

        String accessToken = jwtUtil.createToken(member.getId(), member.getEmail());

        return new MemberResponseDto(accessToken);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        // TODO: 이후에 '이메일:생성 가능 아이디'가 1:N인지, 1:1인지 따로 조건이 없으므로 변경 필요할 수 있음. (현재는 1:1이라고 가정)
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!sha256Util.encrypt(requestDto.password()).equals(member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtUtil.createToken(member.getId(), member.getEmail());

        return new MemberResponseDto(accessToken);
    }

    @Override
    public void changePassword(MemberPasswordChangeDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        int changeRow = memberRepository.changePassword(
            new Member(requestDto.email(), sha256Util.encrypt(requestDto.beforePassword())),
            sha256Util.encrypt(requestDto.afterPassword()));

        if (changeRow <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void resetPassword(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        // TODO: 주어진 이메일에 대해 전송 후, 사용자에게 인증받는 절차는 거쳤다고 가정

        memberRepository.resetPassword(
            new Member(requestDto.email(), sha256Util.encrypt(requestDto.password())));
    }

    @Override
    public MemberCredentialDto findById(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        return new MemberCredentialDto(member.getId(), member.getEmail(), member.getPassword());
    }
}
