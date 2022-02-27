package com.inhabas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.TokenService;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.jwtUtils.TokenDto;
import com.inhabas.testConfig.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@NoSecureWebMvcTest(JwtTokenController.class)
public class JwtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @Spy
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("유효하지 않은 토큰 엔드포인트는, 401")
    @Test
    public void invalidTokenTest() throws Exception {
        //when
        mockMvc.perform(get("/jwt/test-invalid-token"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @DisplayName("accessToken 재발급")
    @Test
    public void reissueAccessTokenTest() throws Exception {
        //given
        TokenDto tmp = jwtTokenProvider.createJwtToken(1, Role.BASIC_MEMBER.toString(), null);
        TokenDto expectedNewTokenDto = new TokenDto(tmp.getGrantType(), tmp.getAccessToken(), null, tmp.getExpiresIn());

        given(tokenService.reissueAccessToken(any())).willReturn(expectedNewTokenDto);

        //when
        mockMvc.perform(post("/jwt/reissue-token").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedNewTokenDto)))
                .andReturn();
    }

}
