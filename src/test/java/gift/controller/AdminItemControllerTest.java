package gift.controller;

import gift.Jwt.JwtUtil;
import gift.Jwt.TokenUtils;
import gift.dto.itemDto.ItemCreateDto;
import gift.entity.User;
import gift.entity.UserRole;
import gift.service.itemService.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class AdminItemControllerTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void 카카오상품저장하기() {
        ItemCreateDto dto = new ItemCreateDto("카카오", 1500, "juice.png", true);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

    @Test
    void 카카오상품_저장하기예외처리(){ // 의도와 다르게 작동함 -> 오류수정 필요
        ItemCreateDto dto = new ItemCreateDto("카카오", 1500, "juice.png", false);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

    @Test
    void 이메일추출_성공() {

        User user = new User(1L, "tester@example.com", "securePassword", UserRole.USER);
        String token = jwtUtil.generateToken(user);

        String extractedEmail = tokenUtils.extractEmail(token);

        assertThat(extractedEmail).isEqualTo("tester@example.com");
    }

}




