package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.jwt.JwtFilter;
import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtUtil;
import gift.common.code.CustomResponseCode;
import gift.common.dto.CustomResponseBody;
import gift.common.exception.CustomException;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.User;
import gift.service.WishService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(WishController.class)
@Import({JwtFilter.class, JwtProvider.class, WishControllerTest.JwtTestConfig.class})
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private WishService wishService;

    private final User mockUser = new User(1L, "test@domain.com", "pw");

    @TestConfiguration
    static class JwtTestConfig {

        @Bean
        public JwtUtil jwtUtil() {
            return new JwtUtil("testtesttesttesttesttesttesttest");
        }
    }

    @Test
    @DisplayName("위시 등록 성공")
    void testAddWishSuccess() throws Exception {
        WishRequest request = new WishRequest(10L, 2);
        WishResponse response = new WishResponse(1L, 10L, 2, "상품명", 1000, "https://img");

        given(wishService.addWish(eq(mockUser.getId()), any(WishRequest.class))).willReturn(
            response);

        String token = createToken(mockUser.getId(), mockUser.getEmail());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(request)))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<WishResponse> res = objectMapper.readValue(content,
            objectMapper.getTypeFactory()
                .constructParametricType(CustomResponseBody.class, WishResponse.class));

        assertWishResponse(res, CustomResponseCode.CREATED);

        WishResponse data = res.data();
        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data).isNotNull(),
            () -> assertThat(data.productId()).isEqualTo(10L),
            () -> assertThat(data.quantity()).isEqualTo(2),
            () -> assertThat(data.productName()).isEqualTo("상품명"),
            () -> assertThat(data.price()).isEqualTo(1000),
            () -> assertThat(data.imageUrl()).isEqualTo("https://img")
        );
    }

    @Test
    @DisplayName("위시 등록 실패 - 이미 등록된 항목")
    void testAddWishDuplicateFail() throws Exception {
        WishRequest request = new WishRequest(10L, 1);

        given(wishService.addWish(eq(mockUser.getId()), any(WishRequest.class)))
            .willThrow(new CustomException(CustomResponseCode.ALREADY_EXISTS));

        String token = createToken(mockUser.getId(), mockUser.getEmail());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(request)))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<?> response = objectMapper.readValue(content, CustomResponseBody.class);
        assertErrorResponse(response, CustomResponseCode.ALREADY_EXISTS);
    }

    @Test
    @DisplayName("위시 등록 실패 - 유효성 검사")
    void testAddWishValidationFail() throws Exception {
        WishRequest invalidRequest = new WishRequest(null, -1);

        String token = createToken(mockUser.getId(), mockUser.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("위시 목록 조회 성공")
    void testGetWishesSuccess() throws Exception {
        WishResponse response = new WishResponse(1L, 10L, 2, "상품명", 1000, "https://img");
        List<WishResponse> wishList = Collections.singletonList(response);

        given(wishService.getWishes(eq(mockUser.getId()))).willReturn(wishList);

        String token = createToken(mockUser.getId(), mockUser.getEmail());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/wishes")
                .header("Authorization", "Bearer " + token))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<List<WishResponse>> res = objectMapper.readValue(content,
            objectMapper.getTypeFactory().constructParametricType(CustomResponseBody.class,
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, WishResponse.class)));

        assertWishResponse(res, CustomResponseCode.RETRIEVED);

        List<WishResponse> data = res.data();
        assertAll("응답 데이터 필드 검증",
            () -> assertThat(data).isNotNull(),
            () -> assertThat(data).anyMatch(w -> w.productId().equals(10L)),
            () -> assertThat(data).anyMatch(w -> w.productName().equals("상품명"))
        );
    }

    @Test
    @DisplayName("위시 목록 조회 실패 - 인증 없음")
    void testGetWishesUnauthorizedFail() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/wishes"))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<?> response = objectMapper.readValue(content, CustomResponseBody.class);
        assertErrorResponse(response, CustomResponseCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("위시 삭제 성공")
    void testDeleteWishSuccess() throws Exception {
        String token = createToken(mockUser.getId(), mockUser.getEmail());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/wishes/{productId}", 10L)
                    .header("Authorization", "Bearer " + token))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<?> res = objectMapper.readValue(content, CustomResponseBody.class);
        assertWishResponse(res, CustomResponseCode.DELETED);
    }

    @Test
    @DisplayName("위시 삭제 실패 - 존재하지 않는 wish")
    void testDeleteWishNotFoundFail() throws Exception {
        doThrow(new CustomException(CustomResponseCode.NOT_FOUND))
            .when(wishService).deleteWish(eq(mockUser.getId()), eq(999L));

        String token = createToken(mockUser.getId(), mockUser.getEmail());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/wishes/{productId}", 999L)
                    .header("Authorization", "Bearer " + token))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        CustomResponseBody<?> response = objectMapper.readValue(content, CustomResponseBody.class);
        assertErrorResponse(response, CustomResponseCode.NOT_FOUND);
    }

    private <T> void assertWishResponse(CustomResponseBody<T> response,
        CustomResponseCode expectedCode) {
        assertAll("응답 객체 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.status()).isEqualTo(expectedCode.getCode())
        );
    }

    private void assertErrorResponse(CustomResponseBody<?> response,
        CustomResponseCode expectedCode) {
        assertAll("응답 객체 검증",
            () -> assertThat(response).isNotNull(),
            () -> assertThat(response.status()).isEqualTo(expectedCode.getCode()),
            () -> assertThat(response.message()).isEqualTo(expectedCode.getMessage())
        );
    }

    private String createToken(Long userId, String email) {
        return jwtUtil.generateToken(email, userId);
    }
}
