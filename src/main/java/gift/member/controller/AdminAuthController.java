package gift.member.controller;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.service.MemberManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin/members")
public class AdminAuthController {

    private final MemberManagementService memberManagementService;

    public AdminAuthController(MemberManagementService memberManagementService) {
        this.memberManagementService = memberManagementService;
    }

    @GetMapping
    public String showMembers(Model model) {
        List<MemberResponseDto> members = memberManagementService.getAllMembers();
        model.addAttribute("members", members);
        return "admin/member-list";
    }

    // 회원 추가
    @PostMapping
    public String addMember(@ModelAttribute MemberRequestDto memberRequestDto) {
        memberManagementService.addMember(memberRequestDto);
        return "redirect:/api/admin/members";
    }

    // 회원 수정
    @PutMapping("/{id}")
    public String updateMember(
            @PathVariable Long id,
            MemberRequestDto requestDto
    ) {
        memberManagementService.updateMember(id, requestDto);
        return "redirect:/api/admin/members";
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberManagementService.deleteMember(id);
        return "redirect:/api/admin/members";
    }


}
