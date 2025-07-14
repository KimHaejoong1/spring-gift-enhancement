package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.*;
import gift.jwt.JwtProvider;
import gift.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository,  JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }


    public MemberResponseDto create(MemberRequestDto requestDto) {
        Optional<Member> existedMember = memberRepository.findByEmail(requestDto.email());
        if (existedMember.isPresent()) {
            throw new DuplicateMemberException(requestDto.email());
        }

        Member member = new Member(requestDto.name(), requestDto.email(), requestDto.password());
        Member newMember = memberRepository.save(member);

        return new MemberResponseDto(newMember.getId(), newMember.getName(), newMember.getEmail(), newMember.getPassword());
    }

    public String login(@Valid MemberLoginRequestDto requestDto) {
        Member existedMember = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new MemberNotFoundException("email", requestDto.email()));

        if (existedMember.getPassword().equals(requestDto.password())) {
             return jwtProvider.generateToken(existedMember);
        }
        else{
            throw new InvalidPasswordException();
        }
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream()
                .map(m -> new MemberResponseDto(m.getId(), m.getName(), m.getEmail(), m.getPassword()))
                .toList();
    }

    public MemberResponseDto find(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberNotFoundException("id", memberId.toString()));

        return new MemberResponseDto(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public MemberResponseDto update(Long memberId, @Valid MemberRequestDto requestDto) {
        Member member = memberRepository.update(memberId, requestDto.name(), requestDto.email(), requestDto.password())
                .orElseThrow(() -> new MemberNotFoundException("id", memberId.toString()));
        return new MemberResponseDto(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public void delete(Long memberId) {
        boolean deleted = memberRepository.deleteById(memberId);
        if (!deleted) {
            throw new MemberNotFoundException("id", memberId.toString());
        }
    }

}
