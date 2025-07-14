package gift.domain;

import org.mindrot.jbcrypt.BCrypt;

public class Member {
    private final Long id;
    private final String email;
    private final String passwordHash;

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public boolean validatePlainPassword(String password) {
        return BCrypt.checkpw(password, passwordHash);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Member(String email, String password) {
        this.id = -1L;
        this.email = email;
        this.passwordHash = BCrypt.hashpw(
                password,
                BCrypt.gensalt());
    }

    public Member(Long id, String email, String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
