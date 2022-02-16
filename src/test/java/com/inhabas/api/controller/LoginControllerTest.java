package com.inhabas.api.controller;

import com.inhabas.api.domain.MemberTest;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.security.annotataion.WithMockCustomOAuth2Account;
import com.inhabas.api.security.domain.RefreshTokenService;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.jwtUtils.TokenDto;
import com.inhabas.testConfig.DefaultWebMvcTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultWebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private MemberService memberService;

    @Test
    @WithMockCustomOAuth2Account
    public void OAuth2_인증_후_기존회원에게_토큰을_발급한다() throws Exception {

        //given
        TokenDto expectedReturnToken
                = new TokenDto("Bearer", "test access token", "test refresh token", 180000L);
        given(tokenProvider.createJwtToken(anyInt(), anyString(), any())).willReturn(expectedReturnToken);
        given(memberService.findById(anyInt())).willReturn(MemberTest.MEMBER1);

        //when
        MvcResult response = mockMvc.perform(get("/login/test-success"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //then
        String redirectedUrl = response.getResponse().getRedirectedUrl();
        Assertions.assertThat(redirectedUrl)
                .contains("/login/success", "accessToken=", "refreshToken=", "expiresIn=");
    }

    @Test
    @WithMockCustomOAuth2Account(alreadyJoined = false)
    public void OAuth2_인증_후_신규회원을_회원가입_페이지로_이동시킨다() throws Exception {

        //given
        TokenDto expectedReturnToken
                = new TokenDto("Bearer", "test access token", "test refresh token", 180000L);
        given(tokenProvider.createJwtToken(anyInt(), anyString(), any())).willReturn(expectedReturnToken);

        //when
        MvcResult response = mockMvc.perform(get("/login/test-success"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //then
        String redirectedUrl = response.getResponse().getRedirectedUrl();
        Assertions.assertThat(redirectedUrl)
                .contains("/signUp", "accessToken=", "expiresIn=");
    }

}
