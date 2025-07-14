package gift.member.entity;

import gift.member.util.PasswordUtil;

public class Member {
    private Long id;
    private String email;
    private String salt;
    private String password;
    private String role;

    public Member(Long id, String email, String salt, String password, String role) {
        this.id = id;
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.role = role;
    }

    public Member(String email, String salt, String password, String role) {
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.role = role;
    }

    public Long getId() {return id;}
    public String getEmail() {return email;}
    public String getSalt() {return salt;}
    public String getPassword() {return password;}
    public String getRole() {return role;}

    public void verifyPassword(String rawPassword, PasswordUtil passwordUtil) {
        String hashedPassword = passwordUtil.hashPassword(rawPassword, this.salt);

        if (!hashedPassword.equals(this.password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }
}
