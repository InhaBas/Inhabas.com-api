package com.inhabas.api.controller;

import com.inhabas.api.service.login.LoginService;
import com.inhabas.security.annotataion.WithMockCustomOAuth2Account;
import com.inhabas.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DefaultWebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    @WithMockCustomOAuth2Account
    public void OAuth2_인증이_성공적으로_완료됐다() throws Exception {

        //given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("http://localhost:8080/login/success"));
        given(loginService.prepareRedirectHeader(any(), any())).willReturn(httpHeaders);

        //when
        MvcResult response = mockMvc.perform(get("/login/test-success"))
                .andExpect(status().isSeeOther())  // 303
                .andExpect(header().string("location", "http://localhost:8080/login/success"))
                .andReturn();

        then(loginService).should(times(1)).prepareRedirectHeader(any(), any());
    }

}
