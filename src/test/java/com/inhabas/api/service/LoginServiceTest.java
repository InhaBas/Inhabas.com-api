package com.inhabas.api.service;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.TokenService;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.service.login.LoginServiceImpl;
import com.inhabas.api.service.login.OriginProviderForProduction;
import com.inhabas.api.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private TokenService tokenService;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private OriginProviderForProduction originProvider;

    @Spy
    private JwtTokenProvider tokenProvider;


    @DisplayName("로그인 성공 시 리다이렉트 url 을 점검")
    @Test
    public void loginSuccessRedirectUrlTest() {

        String login_success_redirect_url = (String) ReflectionTestUtils.getField(loginService, "LOGIN_SUCCESS_REDIRECT_URL");

        assertThat(login_success_redirect_url).isEqualTo("%s/login/success");
    }

    @DisplayName("회원가입 필요 시 리다이렉트 url 을 점검")
    @Test
    public void signUpRedirectUrlTest() {

        String login_success_redirect_url = (String) ReflectionTestUtils.getField(loginService, "SIGNUP_REQUIRED_REDIRECT_URL");

        assertThat(login_success_redirect_url).isEqualTo("%s/signUp");
    }

    @DisplayName("회원가입 리다이렉트를 위한 헤더가 잘 만들어지는지 테스트")
    @Test
    public void prepareSignUpRedirectHeaderTest() throws URISyntaxException {

        //given
        AuthUserDetail authUserDetail = new AuthUserDetail(2, "google", "my@gmail.com", null, false, true);
        given(originProvider.getOrigin(any())).willReturn(new StringBuffer("https://inhabas.com"));

        //when
        HttpHeaders httpHeaders = loginService.prepareRedirectHeader(request, authUserDetail);

        //then
        assertThat(httpHeaders.keySet())
                .contains("accessToken", "expiresIn", "Location", "refreshToken", "profileImageUrl");

        assertThat(httpHeaders.getFirst("accessToken")).hasSizeGreaterThan(2); // jwt 토큰은 못해도 점 두개는 있어야함.
        assertThat(httpHeaders.getFirst("expiresIn")).isNotBlank();
        assertThat(Objects.requireNonNull(httpHeaders.getLocation()).getPath()).isEqualTo("/signUp");

        assertThat(httpHeaders.getFirst("refreshToken")).isBlank();
        assertThat(httpHeaders.getFirst("profileImageUrl")).isBlank();
    }

    @DisplayName("로그인 리다이렉트를 위한 헤더가 잘 만들어지는지 테스트")
    @Test
    public void prepareLoginRedirectHeaderTest() throws URISyntaxException {

        //given
        AuthUserDetail authUserDetail = new AuthUserDetail(2, "google", "my@gmail.com", 12171652, true, true);
        authUserDetail.setProfileImageUrl("https://googlestatic.com/blahblah");
        given(originProvider.getOrigin(any())).willReturn(new StringBuffer("https://inhabas.com"));
        given(memberService.findById(anyInt())).willReturn(
                Member.builder()
                        .id(12171652)
                        .name("유동현")
                        .phone("010-0000-0000")
                        .picture("")
                        .schoolInformation(SchoolInformation.ofStudent("컴퓨터공학과", 3, 2))
                        .ibasInformation(new IbasInformation(Role.BASIC_MEMBER, "", 0))
                        .build()
        );

        //when
        HttpHeaders httpHeaders = loginService.prepareRedirectHeader(request, authUserDetail);

        //then
        assertThat(httpHeaders.keySet())
                .contains("accessToken", "expiresIn", "Location", "refreshToken", "profileImageUrl");

        assertThat(httpHeaders.getFirst("accessToken")).hasSizeGreaterThan(2); // jwt 토큰은 못해도 점 두개는 있어야함.
        assertThat(httpHeaders.getFirst("refreshToken")).hasSizeGreaterThan(2); // jwt 토큰은 못해도 점 두개는 있어야함.
        assertThat(httpHeaders.getFirst("expiresIn")).isNotBlank();
        assertThat(httpHeaders.getFirst("profileImageUrl")).isNotBlank();
        assertThat(Objects.requireNonNull(httpHeaders.getLocation()).getPath()).isEqualTo("/login/success");
    }

    @DisplayName("OAuth2 인증을 거치지 않고 url jumping 시도하는 경우, AccessDenyException 발생")
    @Test
    public void urlJumpingNotAllowedTest() {

        //given
        AuthUserDetail authUserDetail = null;

        //when
        assertThrows(AccessDeniedException.class,
                () -> loginService.prepareRedirectHeader(request, authUserDetail));
    }
}
