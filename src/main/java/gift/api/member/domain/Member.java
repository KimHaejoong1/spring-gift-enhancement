package gift.api.member.domain;

public class Member {

    private Long id;
    private String email;
    private String password;
    private MemberRole role;

    public Member(Long id, String email, String password, MemberRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }
}
