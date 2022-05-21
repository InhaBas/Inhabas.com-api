package com.inhabas.api.auth.domain.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest
public class JwtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Spy
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Disabled
    @DisplayName("accessToken 재발급")
    @Test
    public void reissueAccessTokenTest() throws Exception {
        //given
        String tmp = jwtTokenProvider.createAccessToken(null);
        //TokenDto expectedNewTokenDto = new TokenDto(tmp.getGrantType(), tmp.getAccessToken(), null, tmp.getExpiresIn());
//
//        given(tokenService.reissueAccessToken(any())).willReturn(expectedNewTokenDto);
//
//        //when
//        mockMvc.perform(post("/jwt/reissue-token").with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(expectedNewTokenDto)))
//                .andReturn();
    }

}
