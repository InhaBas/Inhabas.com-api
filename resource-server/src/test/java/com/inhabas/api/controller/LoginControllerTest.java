package com.inhabas.api.controller;

import com.inhabas.annotataion.WithMockCustomOAuth2Account;
import com.inhabas.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@DefaultWebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Disabled
    @Test
    @WithMockCustomOAuth2Account
    public void OAuth2_인증이_성공적으로_완료됐다() throws Exception {

//        //given
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(URI.create("http://localhost:8080/login/success"));
//        given(loginService.prepareRedirectHeader(any(), any())).willReturn(httpHeaders);
//
//        //when
//        MvcResult response = mockMvc.perform(get("/login/test-success"))
//                .andExpect(status().isSeeOther())  // 303
//                .andExpect(header().string("location", "http://localhost:8080/login/success"))
//                .andReturn();
//
//        then(loginService).should(times(1)).prepareRedirectHeader(any(), any());
    }

}
