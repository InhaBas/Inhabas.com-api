package com.inhabas.api.service.login;

import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import com.inhabas.api.security.domain.token.RefreshToken;
import com.inhabas.api.security.service.TokenService;
import com.inhabas.api.security.domain.token.TokenDto;
import com.inhabas.api.security.domain.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * 토큰을 response header 로 넘김
 */
@RequiredArgsConstructor
public class LoginRedirectHeaderHeaderProvider implements LoginRedirectHeaderProvider {

    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final HttpOriginProvider originProvider;

    public HttpHeaders prepareLoginRedirectHeader(HttpServletRequest request, String temporaryRedirectUri, String permanentRedirectUri, AuthUserDetail authUserDetail, String memberRole) throws URISyntaxException {

        TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), memberRole, null); // 추후 팀 작업 시 변경해야함.
        tokenService.saveRefreshToken(new RefreshToken(jwtToken.getRefreshToken()));

        return getRedirectHttpHeaders(temporaryRedirectUri, originProvider.getOrigin(request),
                jwtToken.getAccessToken(), jwtToken.getRefreshToken(), String.valueOf(jwtToken.getExpiresIn()), authUserDetail.getProfileImageUrl(), permanentRedirectUri);
    }

    public HttpHeaders prepareSignUpRedirectHeader(HttpServletRequest request, String temporaryRedirectUri, String permanentRedirectUri, AuthUserDetail authUserDetail) throws URISyntaxException {
        /* 회원가입 필요 */

        TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), Role.ANONYMOUS.toString(), null);

        return getRedirectHttpHeaders(temporaryRedirectUri, originProvider.getOrigin(request),
                jwtToken.getAccessToken(), "", String.valueOf(jwtToken.getExpiresIn()), "", permanentRedirectUri);
    }

    /**
     * service 단에서 개발환경에 따라 origin 갖고 오는거 분리하기.
     */
    private HttpHeaders getRedirectHttpHeaders(String redirectUrl, StringBuffer origin, String accessToken, String refreshToken, String expiresIn, String profileImageUrl, String permanentRedirectUri) throws URISyntaxException {

        URI redirectUri = new URI(String.format(redirectUrl, origin));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(redirectUri);
        httpHeaders.set("accessToken", accessToken);
        httpHeaders.set("refreshToken", refreshToken);
        httpHeaders.set("expiresIn", expiresIn);
        httpHeaders.set("profileImageUrl", profileImageUrl);
        httpHeaders.set("redirectUri", permanentRedirectUri);

        return httpHeaders;
    }

}
