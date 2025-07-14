package gift.member.dto;


import gift.domain.Role;

import java.util.UUID;

public class MemberResponse {

    private UUID id;
    private String email;
    private Role role;

    public MemberResponse(UUID id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    protected MemberResponse(){}

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
