package com.inhabas.api.service;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.service.login.*;
import com.inhabas.api.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoginRedirectHeaderProvider loginRedirectHeaderProvider;

    @DisplayName("OAuth2 인증 후 프론트가 임시 처리해야 할 페이지 url 점검")
    @Test
    public void temporaryRedirectUrlTest() {

        String login_success_redirect_url = (String) ReflectionTestUtils.getField(loginService, "TEMPORARY_REDIRECT_URL");

        assertThat(login_success_redirect_url).isEqualTo("%s/login/success");
    }

    @DisplayName("로그인 성공 시 최종 리다이렉트 url 을 점검")
    @Test
    public void loginSuccessRedirectUrlTest() {

        String login_success_redirect_url = (String) ReflectionTestUtils.getField(loginService, "LOGIN_SUCCESS_REDIRECT_URI");

        assertThat(login_success_redirect_url).isEqualTo("/");
    }

    @DisplayName("회원가입 필요 시 최종 리다이렉트 url 을 점검")
    @Test
    public void signUpRedirectUrlTest() {

        String login_success_redirect_url = (String) ReflectionTestUtils.getField(loginService, "SIGNUP_REQUIRED_REDIRECT_URI");

        assertThat(login_success_redirect_url).isEqualTo("/signUp");
    }

    @DisplayName("회원가입 리다이렉트를 위한 헤더가 잘 만들어지는지 테스트")
    @Test
    public void prepareSignUpRedirectHeaderTest() throws URISyntaxException {

        //given
        AuthUserDetail authUserDetail = new AuthUserDetail(2, "google", "my@gmail.com", null, false, true);

        HttpHeaders expectedHttpHeaders = new HttpHeaders();
        expectedHttpHeaders.setLocation(URI.create("https://inhabas.com/login/success?access_token=123.345.789&refresh_token=&profile_image_url=&expires_in=3600"));
        given(loginRedirectHeaderProvider.prepareSignUpRedirectHeader(any(), any(), any(), any())).willReturn(expectedHttpHeaders);

        //when
        HttpHeaders httpHeaders = loginService.prepareRedirectHeader(request, authUserDetail);

        //then
        assertThat(httpHeaders.keySet()).containsOnly("Location");
        then(loginRedirectHeaderProvider).should(times(1)).prepareSignUpRedirectHeader(any(), any(), any(), any());
    }

    @DisplayName("로그인 리다이렉트를 위한 헤더가 잘 만들어지는지 테스트")
    @Test
    public void prepareLoginRedirectHeaderTest() throws URISyntaxException {

        //given
        AuthUserDetail authUserDetail = new AuthUserDetail(2, "google", "my@gmail.com", 12171652, true, true);
        authUserDetail.setProfileImageUrl("https://googlestatic.com/blahblah");

        HttpHeaders expectedHttpHeaders = new HttpHeaders();
        expectedHttpHeaders.setLocation(URI.create("https://inhabas.com/login/success?access_token=123.345.789&refresh_token=&profile_image_url=&expires_in=3600"));
        given(loginRedirectHeaderProvider.prepareLoginRedirectHeader(any(), any(), any(), any(), any())).willReturn(expectedHttpHeaders);

        given(memberService.findById(anyInt())).willReturn(
                Member.builder()
                        .id(12171652)
                        .name("유동현")
                        .phone("010-0000-0000")
                        .picture("")
                        .schoolInformation(SchoolInformation.ofStudent("컴퓨터공학과", 3))
                        .ibasInformation(new IbasInformation(Role.BASIC_MEMBER, "", 0))
                        .build()
        );

        //when
        HttpHeaders httpHeaders = loginService.prepareRedirectHeader(request, authUserDetail);

        //then
        assertThat(httpHeaders.keySet()).containsOnly("Location");
        then(memberService).should(times(1)).findById(anyInt());
        then(loginRedirectHeaderProvider).should(times(1)).prepareLoginRedirectHeader(any(), any(), any(), any(), any());
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
