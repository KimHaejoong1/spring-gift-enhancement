package gift;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.wishlist.WishListController;
import gift.dto.member.MemberCredentialDto;
import gift.exception.UnAuthenicatedException;
import gift.resolver.LoginMemberArgumentResolver;
import gift.service.member.MemberService;
import gift.service.wishlist.WishListService;
import gift.util.JwtUtil;
import gift.util.Sha256Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = WishListController.class)
@Import({LoginMemberArgumentResolver.class})
public class WishListControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private Sha256Util sha256Util;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private WishListService wishListService;

    @Test
    @DisplayName("위시리스트 생성 - 성공")
    void createWishTest_success() throws Exception {
        String token = "fake-jwt-token";
        Long memberId = 1L;
        String email = "example@naver.com";
        String encryptedPassword = sha256Util.encrypt("qwer");

        given(jwtUtil.getMemberIdFromToken(token))
            .willReturn(memberId);
        given(memberService.findById(memberId))
            .willReturn(new MemberCredentialDto(memberId, email, encryptedPassword));

        mockmvc.perform(post("/api/wishes/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("위시리스트 생성 - 실패")
    void createWishTest_fail() throws Exception {
        String token = "fake-jwt-token";

        given(jwtUtil.getMemberIdFromToken(token))
            .willThrow(new UnAuthenicatedException());

        mockmvc.perform(post("/api/wishes/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("위시리스트 조회 - 성공")
    void findAllWishTest_success() throws Exception {
        String token = "fake-jwt-token";
        Long memberId = 1L;
        String email = "example@naver.com";
        String encryptedPassword = sha256Util.encrypt("qwer");

        given(jwtUtil.getMemberIdFromToken(token))
            .willReturn(memberId);
        given(memberService.findById(memberId))
            .willReturn(new MemberCredentialDto(memberId, email, encryptedPassword));

        mockmvc.perform(get("/api/wishes")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("위시리스트 조회 - 실패")
    void findAllWishTest_fail() throws Exception {
        String token = "fake-jwt-token";
        Long memberId = 1L;
        String email = "example@naver.com";
        String encryptedPassword = sha256Util.encrypt("qwer");

        given(jwtUtil.getMemberIdFromToken(token))
            .willThrow(new UnAuthenicatedException());

        mockmvc.perform(get("/api/wishes")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("위시리스트 삭제 - 성공")
    void deleteWishTest_success() throws Exception {
        String token = "fake-jwt-token";
        Long memberId = 1L;
        String email = "example@naver.com";
        String encryptedPassword = sha256Util.encrypt("qwer");
        
        given(jwtUtil.getMemberIdFromToken(token))
            .willReturn(memberId);
        given(memberService.findById(memberId))
            .willReturn(new MemberCredentialDto(memberId, email, encryptedPassword));
        
        mockmvc.perform(delete("/api/wishes/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("위시리스트 삭제 - 실패")
    void deleteWishTest_fail() throws Exception {
        String token = "fake-jwt-token";
        Long memberId = 1L;
        String email = "example@naver.com";
        String encryptedPassword = sha256Util.encrypt("qwer");

        given(jwtUtil.getMemberIdFromToken(token))
            .willThrow(new UnAuthenicatedException());

        mockmvc.perform(delete("/api/wishes/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }
}