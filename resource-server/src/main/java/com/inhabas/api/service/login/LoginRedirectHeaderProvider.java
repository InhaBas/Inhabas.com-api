package com.inhabas.api.service.login;

import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

public interface LoginRedirectHeaderProvider {

    HttpHeaders prepareLoginRedirectHeader(HttpServletRequest request, String temporaryRedirectUri, String permanentRedirectUri, AuthUserDetail authUserDetail, String memberRole) throws URISyntaxException;

    HttpHeaders prepareSignUpRedirectHeader(HttpServletRequest request, String temporaryRedirectUri, String permanentRedirectUri, AuthUserDetail authUserDetail) throws URISyntaxException;
}
