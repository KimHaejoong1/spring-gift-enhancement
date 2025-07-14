package gift.controller;

import gift.common.code.CustomResponseCode;
import gift.common.dto.CustomResponseBody;
import gift.dto.AuthRequest;
import gift.dto.AuthResponse;
import gift.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponseBody<Void>> register(
        @Valid @RequestBody AuthRequest request) {
        authService.register(request);

        return ResponseEntity
            .status(CustomResponseCode.CREATED.getHttpStatus())
            .body(CustomResponseBody.of(CustomResponseCode.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponseBody<AuthResponse>> login(
        @Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);

        return ResponseEntity
            .status(CustomResponseCode.LOGIN_SUCCESS.getHttpStatus())
            .body(CustomResponseBody.of(CustomResponseCode.LOGIN_SUCCESS, response));
    }
}
