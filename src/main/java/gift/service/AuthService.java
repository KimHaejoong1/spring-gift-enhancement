package gift.service;

import gift.dto.AuthRequest;
import gift.dto.AuthResponse;

public interface AuthService {

    void register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
