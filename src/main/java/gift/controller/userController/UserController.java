package gift.controller.userController;


import gift.Jwt.TokenUtils;
import gift.dto.userDto.UserLoginDto;
import gift.dto.userDto.UserRegisterDto;
import gift.dto.userDto.UserResponseDto;
import gift.dto.userDto.UserUpdateDto;
import gift.service.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TokenUtils tokenUtils;

    public UserController(UserService userService,TokenUtils tokenUtils) {
        this.userService = userService;
        this.tokenUtils = tokenUtils;
    }

    /***
     * Todo. 관리자 User 관리 페이지를 위한 model 생성
     *
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid UserRegisterDto dto) {
        String token = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid UserLoginDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", userService.loginUser(dto)));
    }


    @GetMapping()
    public ResponseEntity<?> getUserList(@RequestHeader("Authorization") String authHeader, @RequestParam(required = false) String email, Model model) {
        String token = tokenUtils.extractToken(authHeader);
        tokenUtils.validateToken(token);
        boolean isAdmin = tokenUtils.requireAdmin(token);

        List<UserResponseDto> users = userService.getUserList(email, isAdmin);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader, @RequestParam Long id, Model model) {
        String token = tokenUtils.extractToken(authHeader);
        tokenUtils.validateToken(token);
        boolean isAdmin = tokenUtils.requireAdmin(token);

        userService.deleteUserById(id, isAdmin);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UserResponseDto> updateUser(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        String token = tokenUtils.extractToken(authHeader);
        tokenUtils.validateToken(token);
        boolean isAdmin = tokenUtils.requireAdmin(token);

        UserResponseDto updatedUser = userService.updateUser(id, dto,isAdmin);

        return ResponseEntity.ok(updatedUser);
    }
}
