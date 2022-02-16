package com.inhabas.security.argumentResolver;

import com.inhabas.api.security.argumentResolver.AuthUserArgumentResolver;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    @Mock
    private AuthUserService authUserService;

    @InjectMocks
    private AuthUserArgumentResolver authUserArgumentResolver;


    @DisplayName("인증이 이루어지지 않았으면, null을 반환한다.")
    @Test
    public void returnNullIfNotAuthenticated() {
        //when
        AuthUser invalidUser = (AuthUser) authUserArgumentResolver.resolveArgument(parameter, null, request, null);

        //then
        assertThat(invalidUser).isNull();
    }

    @DisplayName("Jwt token 인증된 authUser 를 컨트롤러 파라미터로 주입한다.")
    @Test
    public void successToInjectJwtTokenAuthenticatedAuthUserIntoArguments() {
        //given
        //기존 회원 정보
        Integer authUserId = 1;
        AuthUser expectedUser = new AuthUser("google", "my@email.com");
        ReflectionTestUtils.setField(expectedUser, "id", authUserId);

        // jwt 토큰 인증 결과
        JwtAuthenticationToken authentication =
                new JwtAuthenticationToken(expectedUser, Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")));

        //authentication 객체를 컨텍스트에 설정. 최종 인증 끝
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        AuthUser authenticatedUser = (AuthUser) authUserArgumentResolver.resolveArgument(parameter, null, request, null);

        //then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getId()).isEqualTo(authUserId);
        assertThat(authenticatedUser.getEmail()).isEqualTo("my@email.com");
        assertThat(authenticatedUser.getProvider()).isEqualTo("google");
    }

    @DisplayName("OAuth2 인증된 authUser 를 컨트롤러 파라미터로 주입한다.")
    @Test
    public void successToInjectOAuth2AuthenticatedAuthUserIntoArguments() {

        //given
        //기존 회원 정보
        Integer authUserId = 1;
        HashMap<String, Object> attributes = new HashMap<>() {{
            put("name", "김아무개");
            put("email", "my@email.com");
            put("picture", "https://my_photo.com");
        }};
        //OAuth2 인증 결과
        CustomOAuth2User principal = new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email",
                authUserId,
                true);
        //OAuth2 인증결과를 authentication 객체에 담는다.
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                "google");
        //authentication 객체를 컨텍스트에 설정. 최종 인증 끝
        SecurityContextHolder.getContext().setAuthentication(token);

        //기존 유저 정보를 불러오는 행위를 mocking 한다.
        AuthUser expectedUser = new AuthUser("google", "my@email.com");
        ReflectionTestUtils.setField(expectedUser, "id", authUserId);
        given(authUserService.loadUser(authUserId)).willReturn(expectedUser);

        //when
        AuthUser authenticatedUser = (AuthUser) authUserArgumentResolver.resolveArgument(parameter, null, request, null);

        //then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getId()).isEqualTo(authUserId);
        assertThat(authenticatedUser.getEmail()).isEqualTo("my@email.com");
        assertThat(authenticatedUser.getProvider()).isEqualTo("google");
    }


}
