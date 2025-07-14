package gift.service;

import gift.config.NotMatchPasswordException;
import gift.domain.Member;
import gift.domain.Product;
import gift.dto.*;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.util.ShaUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public MemberService(MemberRepository repository, ProductRepository productRepository) {
        this.memberRepository = repository;
        this.productRepository = productRepository;
    }

    public CreateMemberResponse register(CreateMemberRequest request) {
        duplicateEmailCheck(request.email());
        String salt = ShaUtil.getSalt();
        String encryptPassword = ShaUtil.encrypt(request.password(), salt);
        Member member = memberRepository.save(request.email(), encryptPassword, salt);
        return new CreateMemberResponse(member.getId(), member.getEmail());
    }

    public void login(LoginMemberRequest request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.email());
        if (findMember.isEmpty()) {
            throw new NoSuchElementException("Email이 존재하지 않습니다.");
        }
        Member member = findMember.get();
        String requestPassword = ShaUtil.encrypt(request.password(), member.getSalt());
        if (!member.getPassword().equals(requestPassword)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public List<Member> memberList() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        findByIdOrThrow(id);
        return memberRepository.findById(id).get();
    }

    public UpdateMemberResponse update(Long id, UpdateMemberRequest request) {
        findByIdOrThrow(id);
        duplicateEmailCheck(request.email());
        String salt = ShaUtil.getSalt();
        String encryptPassword = ShaUtil.encrypt(request.password(), salt);
        Member updateMember = memberRepository.update(id, request.email(), encryptPassword, salt);
        return new UpdateMemberResponse(id, updateMember.getEmail(), updateMember.getPassword());
    }

    private void findByIdOrThrow(Long id) {
        Optional<Member> byEmail = memberRepository.findById(id);
        if (byEmail.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 멤버입니다.");
        }
    }

    private void duplicateEmailCheck(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new DuplicateKeyException("이미 존재하는 email입니다.");
        }
    }

    public void delete(Long id) {
        memberRepository.delete(id);
    }
}
