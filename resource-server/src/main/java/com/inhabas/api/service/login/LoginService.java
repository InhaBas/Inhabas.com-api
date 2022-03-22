package com.inhabas.api.service.login;

import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

public interface LoginService {

    /**
     *
     * @param request 리다이렉트에 필요한 origin (ex.https://dev.inhabas.com) 을 뽑아오기 위함
     * @param authUserDetail 로그인하는 소셜 계정 정보
     * @return HttpHeader 에 다음과 같은 정보를 설정 <br>
     * (location/accessToken/refreshToken/expiresIn/profileImageUrl) <br>
     * 기존에 회원가입을 완료했던 소셜계정이면 로그인 처리를 위한 리다이렉트, 그게 아니면 회원가입 페이지로 이동
     * @throws URISyntaxException 리다이렉트 uri 설정할 때, 발생할 수 있는 오류
     * @throws AccessDeniedException AuthUserDetail 이 null 인 경우, OAuth2 인증을 거치지 않았다고 판단.
     */
    HttpHeaders prepareRedirectHeader(HttpServletRequest request, AuthUserDetail authUserDetail) throws URISyntaxException;

}
