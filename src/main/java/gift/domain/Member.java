package gift.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String salt;

    public Member(Long id, String email, String password, String salt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
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
}
