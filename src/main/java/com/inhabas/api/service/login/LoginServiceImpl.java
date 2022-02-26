package com.inhabas.api.service.login;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.RefreshToken;
import com.inhabas.api.security.domain.RefreshTokenService;
import com.inhabas.api.security.jwtUtils.TokenDto;
import com.inhabas.api.security.jwtUtils.TokenProvider;
import com.inhabas.api.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String LOGIN_SUCCESS_REDIRECT_URL = "%s/login/success";
    private static final String SIGNUP_REQUIRED_REDIRECT_URL = "%s/signUp";

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final HttpOriginProvider originProvider;

    @Override
    public HttpHeaders prepareRedirectHeader(HttpServletRequest request, AuthUserDetail authUserDetail) throws URISyntaxException {

        if (Objects.isNull(authUserDetail)) {
            throw new AccessDeniedException("Oauth2 인증에 성공했을때만 로그인 가능합니다.");
        }

        HttpHeaders httpHeaders = null;

        if (authUserDetail.hasJoined()) {
            /* 유저 권한 들고오기 (추후 작업이 필요함.)
            - role 은 (미승인회원, 일반회원, 교수, 회장단, 회장) 과 같이 수직구조의 권한 => 상대적으로 덜 변함. => enum 타입
            - team 은 (총무팀, 운영팀, 기획팀, IT팀, 회계) 등 과 같은 수평구조의 권한 => 시간에 따라 더 변하기 쉬움 => db 연동
            */
            Member member = memberService.findById(authUserDetail.getProfileId()); // 권한만 갖고 오도록 리팩토링 필요
            String role = member.getIbasInformation().getRole().toString();
            // List<String> teams = member.getIbasInformation().getTeams().stream().map(t-> t.toString()).collect(Collections.toCollect);

            httpHeaders = this.prepareLoginRedirectHeader(request, authUserDetail, role);
        }
        else {
            httpHeaders = this.prepareSignUpRedirectHeader(request, authUserDetail);
        }

        return httpHeaders;
    }

    private HttpHeaders prepareLoginRedirectHeader(HttpServletRequest request, AuthUserDetail authUserDetail, String memberRole) throws URISyntaxException {

        TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), memberRole, null); // 추후 팀 작업 시 변경해야함.
        refreshTokenService.save(new RefreshToken(jwtToken.getRefreshToken()));

        return getRedirectHttpHeaders(LOGIN_SUCCESS_REDIRECT_URL, originProvider.getOrigin(request),
                jwtToken.getAccessToken(), jwtToken.getRefreshToken(), String.valueOf(jwtToken.getExpiresIn()), authUserDetail.getProfileImageUrl());
    }

    private HttpHeaders prepareSignUpRedirectHeader(HttpServletRequest request, AuthUserDetail authUserDetail) throws URISyntaxException {
        /* 회원가입 필요 */

        TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), Role.ANONYMOUS.toString(), null);

        return getRedirectHttpHeaders(SIGNUP_REQUIRED_REDIRECT_URL, originProvider.getOrigin(request),
                jwtToken.getAccessToken(), "", String.valueOf(jwtToken.getExpiresIn()), "");
    }

    /**
     * service 단에서 개발환경에 따라 origin 갖고 오는거 분리하기.
     */
    private HttpHeaders getRedirectHttpHeaders(String redirectUrl, StringBuffer origin, String accessToken, String refreshToken, String expiresIn, String profileImageUrl) throws URISyntaxException {
        URI redirectUri = new URI(String.format(redirectUrl, origin));
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(redirectUri);
        httpHeaders.set("accessToken", accessToken);
        httpHeaders.set("refreshToken", refreshToken);
        httpHeaders.set("expiresIn", expiresIn);
        httpHeaders.set("profileImageUrl", profileImageUrl);

        return httpHeaders;
    }

}

