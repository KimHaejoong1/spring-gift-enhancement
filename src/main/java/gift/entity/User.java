package gift.entity;

import jakarta.validation.constraints.NotNull;

public record User(Long id, @NotNull String email, @NotNull String password, @NotNull UserRole role) {
    public boolean checkPassword(String password) {
        if (!this.password.equals(password)) {
            return false;
        }
        return true;
    }
}
