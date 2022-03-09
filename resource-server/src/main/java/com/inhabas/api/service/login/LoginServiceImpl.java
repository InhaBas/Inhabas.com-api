package com.inhabas.api.service.login;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String TEMPORARY_REDIRECT_URL = "%s/login/success";
    private static final String LOGIN_SUCCESS_REDIRECT_URI = "/";
    private static final String SIGNUP_REQUIRED_REDIRECT_URI = "/signUp";

    private final MemberService memberService;
    private final LoginRedirectHeaderProvider redirectHeaderProvider;

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

            httpHeaders = redirectHeaderProvider.prepareLoginRedirectHeader(request, TEMPORARY_REDIRECT_URL, LOGIN_SUCCESS_REDIRECT_URI, authUserDetail, role);
        }
        else {
            httpHeaders = redirectHeaderProvider.prepareSignUpRedirectHeader(request, TEMPORARY_REDIRECT_URL, SIGNUP_REQUIRED_REDIRECT_URI, authUserDetail);
        }

        return httpHeaders;
    }
}

