package gift.member.service;

import gift.exception.EmailExistsException;
import gift.exception.MemberNotFoundByEmailException;
import gift.exception.MemberNotFoundByIdException;
import gift.member.dto.request.UpdateRequestDto;
import gift.member.dto.response.MemberResponseDto;
import gift.member.dto.request.RegisterRequestDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.member.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;

    public MemberService(MemberRepository memberRepository, PasswordUtil passwordUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
    }

    public MemberResponseDto register(RegisterRequestDto registerRequestDto) {
        if(memberRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new EmailExistsException();
        }

        String salt = passwordUtil.getSalt();
        String hashedPassword = passwordUtil.hashPassword(registerRequestDto.password(), salt);

        Member member = new Member(
                registerRequestDto.email(),
                salt,
                hashedPassword,
                "USER");

        return MemberResponseDto.from(memberRepository.save(member));
    }

    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundByIdException(id));

        return MemberResponseDto.from(member);
    }

    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundByEmailException(email));

        return member;
    }

    public MemberResponseDto updateMember(Long id, UpdateRequestDto updateRequestDto) {
        Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new MemberNotFoundByIdException(id));

        memberRepository.findByEmail(updateRequestDto.email())
                .filter(foundMember -> !foundMember.getId().equals(id))
                .ifPresent(m -> {
                    throw new EmailExistsException();
                });

        String newPassword = updateRequestDto.password();
        String salt = member.getSalt();
        String hashedPassword = member.getPassword();

        if(newPassword != null && !newPassword.isBlank()){
            salt = passwordUtil.getSalt();
            hashedPassword = passwordUtil.hashPassword(newPassword, salt);
        }

        Member memberToUpdate = new Member(
                id,
                updateRequestDto.email(),
                salt,
                hashedPassword,
                updateRequestDto.role()
        );

        return MemberResponseDto.from(memberRepository.update(memberToUpdate));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
