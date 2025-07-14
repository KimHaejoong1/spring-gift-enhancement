package gift.controller.member;


import gift.domain.Member;
import gift.dto.member.MemberRequest;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class MemberPageController {
    private final MemberService memberService;


    public MemberPageController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 메인 페이지 : 유저 목록 조회
    @GetMapping
    public String findAll(Model model){
        List<Member> list = memberService.findAll();
        model.addAttribute("memberList", list);
        return "member/memberMain";
    }

    // 유저 등록 페이지 이동
    @GetMapping("/new")
    public String showNewMemberForm(
        Model model
    ){
        model.addAttribute("request", MemberRequest.createForNewMemberForm());
        return "member/newMember";
    }

    // 유저 등록
    @PostMapping
    public String createMember(
        @Valid @ModelAttribute("request") MemberRequest request,
        BindingResult bindingResult
    ) {

        if(bindingResult.hasErrors()){
            return "member/newMember";
        }

        memberService.insert(request);

        return "redirect:/admin/members";
    }

    // 유저 수정 페이지로 이동
    @GetMapping("/update/{memberId}")
    public String updateFormMember(
        @PathVariable Long memberId,
        Model model
    ){
        Member member = memberService.findById(memberId);
        MemberRequest request = new MemberRequest(memberId, member.getEmail(), member.getPassword());


        model.addAttribute("request", request);
        return "member/updateMember";
    }

    // 유저 수정(수정 처리)
    @PostMapping("/update/{memberId}")
    public String updateMember(
        @Valid @ModelAttribute("request") MemberRequest request,
        @PathVariable Long memberId,
        BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "member/updateMember";
        }

        memberService.update(memberId, request);

        return "redirect:/admin/members";
    }

    // 유저 삭제
    @PostMapping("/delete/{memberId}")
    public String deleteMember(
        @PathVariable Long memberId
    ){
        memberService.deleteById(memberId);

        return "redirect:/admin/members";
    }

}
