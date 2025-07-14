package gift.member.dto;

import gift.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestDto {

    @NotBlank(message = "이메일을 비울 수 없습니다")
    private String email;
    @NotBlank(message = "비밀번호를 비울 수 없습니다")
    private String password;
    private String role;

    private MemberRequestDto(){};
    private MemberRequestDto(Member member) {
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    public static MemberRequestDto fromEntity(Member member) {
        return new MemberRequestDto(member);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
