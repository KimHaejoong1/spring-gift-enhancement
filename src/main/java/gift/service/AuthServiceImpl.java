package gift.service;

import gift.auth.jwt.JwtUtil;
import gift.common.code.CustomResponseCode;
import gift.common.exception.CustomException;
import gift.dto.AuthRequest;
import gift.dto.AuthResponse;
import gift.entity.User;
import gift.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(CustomResponseCode.EMAIL_DUPLICATE);
        }

        String encrypted = passwordEncoder.encode(request.password());
        User user = new User(null, request.email(), encrypted);
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new CustomException(CustomResponseCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(CustomResponseCode.LOGIN_FAILED);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return AuthResponse.from(token);
    }
}
