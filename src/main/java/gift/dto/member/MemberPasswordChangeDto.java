package gift.dto.member;

public record MemberPasswordChangeDto(String email, String beforePassword, String afterPassword) {

}
