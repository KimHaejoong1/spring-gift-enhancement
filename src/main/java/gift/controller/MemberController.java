package gift.controller;

import gift.domain.Member;
import gift.dto.CreateMemberRequest;
import gift.dto.UpdateMemberRequest;
import gift.service.MemberService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public String memberList(Model model) {
        List<Member> members = service.memberList();
        model.addAttribute("members", members);
        return "member/members";
    }

    @GetMapping("/add")
    public String registerMember(Model model) {
        model.addAttribute("member", new Member(null, "", "", ""));
        return "member/addForm";
    }

    @PostMapping("/add")
    public String registerMember(@Validated @ModelAttribute("member") CreateMemberRequest member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/addForm";
        }
        try {
            service.register(member);
        } catch (DuplicateKeyException e) {
            bindingResult.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
            return "member/addForm";
        }
        return "redirect:/api/members";
    }

    @GetMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, Model model) {
        Member findMember= service.findById(id);
        model.addAttribute("member", findMember);
        return "member/editForm";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Validated @ModelAttribute("member")UpdateMemberRequest request,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/editForm";
        }

        try {
            service.update(id, request);
        } catch (DuplicateKeyException e) {
            bindingResult.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
            return "member/editForm";

        }
        return "redirect:/api/members";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/api/members";
    }
}
