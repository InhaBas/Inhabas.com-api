package com.inhabas.api.controller;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.argumentResolver.Authenticated;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.RefreshToken;
import com.inhabas.api.security.domain.RefreshTokenService;
import com.inhabas.api.security.jwtUtils.TokenDto;
import com.inhabas.api.security.jwtUtils.TokenProvider;
import com.inhabas.api.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private static final String LOGIN_SUCCESS_REDIRECT_URL = "%s/api/login/success?accessToken=%s&refreshToken=%s&expiresIn=%d&profileImageUrl=%s";
    private static final String SIGNUP_REQUIRED_REDIRECT_URL = "%s/signUp?accessToken=%s&expiresIn=%d";

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    /* token authentication */

    @GetMapping("${authenticate.oauth2-success-handle-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public void successLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @Authenticated AuthUserDetail authUserDetail) throws IOException {

        StringBuffer origin = this.getOrigin(request);

        if (authUserDetail.hasJoined()) {

            /* 유저 권한 들고오기 (추후 작업이 필요함.)
            - role 은 (미승인회원, 일반회원, 교수, 회장단, 회장) 과 같이 수직구조의 권한 => 상대적으로 덜 변함. => enum 타입
            - team 은 (총무팀, 운영팀, 기획팀, IT팀, 회계) 등 과 같은 수평구조의 권한 => 시간에 따라 더 변하기 쉬움 => db 연동
            */
            Member member = memberService.findById(authUserDetail.getProfileId()); // 권한만 갖고 오도록 리팩토링 필요
            String role = member.getIbasInformation().getRole().toString();
            // List<String> teams = member.getIbasInformation().getTeams().stream().map(t-> t.toString()).collect(Collections.toCollect);

            TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), role, null); // 변경해야함.
            refreshTokenService.save(new RefreshToken(jwtToken.getRefreshToken()));

            request.getSession().invalidate(); // 프론트 단에서 브라우저 쿠키 JSESSIONID, XSRF-TOKEN 지우는 게 좋을 듯. 상관없긴 한디.

            response.sendRedirect(
                    String.format(LOGIN_SUCCESS_REDIRECT_URL, origin,
                            jwtToken.getAccessToken(), jwtToken.getRefreshToken(), jwtToken.getExpiresIn(), authUserDetail.getProfileImageUrl()));
        }
        else {

            /* 회원가입 필요 */

            TokenDto jwtToken = tokenProvider.createJwtToken(authUserDetail.getId(), Role.ANONYMOUS.toString(), null);

            response.sendRedirect(
                    String.format(SIGNUP_REQUIRED_REDIRECT_URL, origin,
                            jwtToken.getAccessToken(), jwtToken.getExpiresIn()));
        }
    }


    @GetMapping("${authenticate.oauth2-failure-handle-url}")
    @Operation(hidden = true)
    public ResponseEntity<?> failToLogin() {
        Map<String, String> message = new HashMap<>() {{
            put("message","fail_to_social_login");
        }};
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }



    private StringBuffer getOrigin(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(request.getRemoteHost()); // 배포시에는 getServerName 사용해야함
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }

        return url;
    }
}
