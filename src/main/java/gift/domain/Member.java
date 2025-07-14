package gift.domain;

import gift.dto.member.MemberRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Member {

    private Long id;

    private String email;

    private String password;

    public Member(Long id, String email, String password){
        this.id=id;
        this.email=email;
        this.password=password;
    }

    public Long getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public static Member of(String email, String rawPassword, PasswordEncoder passwordEncoder){
        return new Member(null, email, passwordEncoder.encode(rawPassword));
    }

    public void update(MemberRequest request, PasswordEncoder passwordEncoder){
        this.email= request.email();
        this.password=passwordEncoder.encode(request.password());
    }

    public boolean matches(String rawPassword, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
