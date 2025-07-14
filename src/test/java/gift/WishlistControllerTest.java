package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.member.token.TokenProvider;
import gift.product.entity.Product;
import gift.wishlist.controller.WishlistController;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WishlistService wishlistService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private MemberRepository memberRepository;

    private Member testMember = new Member(1L, "test@example.com", "salt", "password", "USER");
    private Product testProduct = new Product(10L, "product", 10000L, "http://image.url", Boolean.FALSE);
    private Product testProduct2 = new Product(11L, "product2", 20000L, "http://image2.url", Boolean.FALSE);
    private String testToken = "token";

    @BeforeEach
    void setUp() {
        given(tokenProvider.isValidToken(any())).willReturn(true);
        given(tokenProvider.getMemberIdFromToken(any())).willReturn(testMember.getId());
        given(memberRepository.findById(testMember.getId())).willReturn(Optional.of(testMember));
    }

    @Test
    void 위시리스트에_상품_추가() throws Exception {
        var wishRequestDto = new WishRequestDto(10L, 2);
        var content = objectMapper.writeValueAsString(wishRequestDto);
        var wishResponseDto = new WishResponseDto(
                1L,
                testMember.getId(),
                testProduct.getId(),
                testProduct.getName(),
                testProduct.getPrice(),
                testProduct.getImageUrl(),
                2);

        given(wishlistService.addWish(anyLong(), any(WishRequestDto.class))).willReturn(wishResponseDto);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/wishlist")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        var actual = objectMapper.readValue(response.getContentAsString(), WishResponseDto.class);
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.memberId()).isEqualTo(testMember.getId());
        assertThat(actual.productId()).isEqualTo(testProduct.getId());
        assertThat(actual.name()).isEqualTo(testProduct.getName());
        assertThat(actual.price()).isEqualTo(testProduct.getPrice());
        assertThat(actual.imageUrl()).isEqualTo(testProduct.getImageUrl());
        assertThat(actual.quantity()).isEqualTo(2);
    }

    @Test
    void 위시리스트_조회() throws Exception {
        var wishResponseDto1 = new WishResponseDto(
                1L,
                testMember.getId(),
                testProduct.getId(),
                testProduct.getName(),
                testProduct.getPrice(),
                testProduct.getImageUrl(),
                2);

        var wishResponseDto2 = new WishResponseDto(
                2L,
                testMember.getId(),
                testProduct2.getId(),
                testProduct2.getName(),
                testProduct2.getPrice(),
                testProduct2.getImageUrl(),
                1);

        List<WishResponseDto> wishlist = Arrays.asList(wishResponseDto1, wishResponseDto2);

        given(wishlistService.getWishesByMemberId(testMember.getId())).willReturn(wishlist);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist")
                        .header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        List<WishResponseDto> actualList = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, WishResponseDto.class));

        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList.get(0).id()).isEqualTo(1L);
        assertThat(actualList.get(0).productId()).isEqualTo(testProduct.getId());
        assertThat(actualList.get(1).id()).isEqualTo(2L);
        assertThat(actualList.get(1).productId()).isEqualTo(testProduct2.getId());
    }

    @Test
    void 위시리스트_삭제() throws Exception {
        Long wishId = 1L;
        doNothing().when(wishlistService).deleteWish(testMember.getId(), wishId);

        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wishlist/{wishId}", wishId)
                        .header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    void 인증_토큰_없이_위시리스트_추가_시_실패() throws Exception {
        var wishRequestDto = new WishRequestDto(1L, 2);
        var content = objectMapper.writeValueAsString(wishRequestDto);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void 인증_토큰_없이_위시리스트_조회_시_실패() throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist"))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void 인증_토큰_없이_위시리스트_삭제_시_실패() throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wishlist/1"))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void 유효하지_않은_토큰으로_위시리스트_조회_시_실패() throws Exception {
        given(tokenProvider.isValidToken(any())).willReturn(false);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist")
                        .header("Authorization", "Bearer invalid-token"))
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(401);
    }
}