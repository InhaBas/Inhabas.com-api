package com.inhabas.api.security.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler(String redirectUrl) {
        setDefaultTargetUrl(redirectUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        /* 여기서 기존에 없던 유저이면, 회원가입 리다이렉트 */
        /* 기존 회원이면 jwt 토큰 발급 리다이렉트.. => 리다이렉트 하고 나면 인증정보가 사라지기 때문에, jwt 토큰 발급 전까지만 세션을 이용할까? */
        /* authentication 에 들어있을 듯? */

        System.out.println("시이이이이이이이바라라아아아아아아아아: ");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
