package com.inhabas.api.web.argumentResolver;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    @InjectMocks
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @AfterEach
    public void clearSecurityContest() {
        SecurityContextHolder.clearContext();
    }


    @DisplayName("인증이 이루어지지 않았으면, null을 반환한다.")
    @Test
    public void returnNullIfNotAuthenticated() {
        //when
        Object invalidUser =loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);

        //then
        assertThat(invalidUser).isNull();
    }

    @Disabled
    @DisplayName("Jwt token 인증된 OAuth2UserInfoAuthentication 를 컨트롤러 파라미터로 주입한다.")
    @Test
    public void successToInjectJwtTokenAuthenticatedAuthUserIntoArguments() {
        //given
        //기존 회원 정보
        String uid = "12943275193";

        // jwt 토큰 인증 결과
        JwtAuthenticationResult authentication =
                new JwtAuthenticationResult(uid, "google", "my@gmail.com", Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")));

        //oauth2Info 객체를 컨텍스트에 설정. 최종 인증 끝
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.doReturn(JwtAuthenticationResult.class).when(parameter).getParameterType();

        //when
        OAuth2UserInfoAuthentication oauth2Info = (OAuth2UserInfoAuthentication) loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);

        //then
        assertThat(oauth2Info).isNotNull();
        assertThat(oauth2Info.getAuthorities())
                .singleElement()
                .extracting("role")
                .isEqualTo("ROLE_MEMBER");
    }

    @Disabled
    @DisplayName("OAuth2 인증된 ResolvedAuthenticationResult 를 컨트롤러 파라미터로 주입한다.")
    @Test
    public void successToInjectOAuth2AuthenticatedAuthUserIntoArguments() {

//        //given
//        //기존 회원 정보
//        Integer authUserId = 1;
//        HashMap<String, Object> attributes = new HashMap<>() {{
//            put("name", "김아무개");
//            put("email", "my@email.com");
//            put("picture", "https://my_photo.com");
//        }};
//        //OAuth2 인증 결과
//        CustomOAuth2User principal = new CustomOAuth2User(
//                List.of(new SimpleGrantedAuthority("ROLE_USER")),
//                attributes,
//                "email",
//                AuthUserDetail.builder()
//                        .id(authUserId)
//                        .email("my@email.com")
//                        .profileId(12171652)
//                        .provider("google")
//                        .hasJoined(true)
//                        .isActive(true)
//                        .build());
//        //OAuth2 인증결과를 authentication 객체에 담는다.
//        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
//                principal,
//                principal.getAuthorities(),
//                "google");
//        //authentication 객체를 컨텍스트에 설정. 최종 인증 끝
//        SecurityContextHolder.getContext().setAuthentication(token);
//
//        doReturn(AuthUserDetail.class).when(parameter).getParameterType();
//
//        //when
//        AuthUserDetail authenticatedUser = (AuthUserDetail) loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);
//
//        //then
//        assertThat(authenticatedUser).isNotNull();
//        assertThat(authenticatedUser.getId()).isEqualTo(authUserId);
//        assertThat(authenticatedUser.getEmail()).isEqualTo("my@email.com");
//        assertThat(authenticatedUser.getProvider()).isEqualTo("google");
    }


    @DisplayName("Jwt 토큰 인증된 회원의 Member Id를 반환한다.")
    @Test
    public void successToInjectJwtTokenIntegerIdIntoArguments() {
        //given
        MemberId memberId = new MemberId(12171652);

        // jwt 토큰 인증 결과
        JwtAuthenticationResult authentication =
                new JwtAuthenticationResult("123135135", "google", "my@gmail.com",  Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")));
        authentication.setPrincipal(memberId);

        //authentication 객체를 컨텍스트에 설정. 최종 인증 끝
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.doReturn(MemberId.class).when(parameter).getParameterType();

        //when
        Object profileId = loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);

        // then
        Assertions.assertThat(profileId).isNotNull();
        Assertions.assertThat(profileId).isEqualTo(memberId);
        Assertions.assertThat(profileId).isInstanceOf(MemberId.class);
    }

    @Disabled
    @DisplayName("OAuth2 인증된 authUser에 대한 Id를 받아온 경우 Profile Id를 반환한다.")
    @Test
    public void successToInjectIntegerIdIntoArgumentsAndReturnProfileId() {
//        //given
//        //기존 회원 정보
//        Integer authUserId = 1;
//        HashMap<String, Object> attributes = new HashMap<>() {{
//            put("name", "김아무개");
//            put("email", "my@email.com");
//            put("picture", "https://my_photo.com");
//        }};
//        //OAuth2 인증 결과
//        CustomOAuth2User principal = new CustomOAuth2User(
//                List.of(new SimpleGrantedAuthority("ROLE_USER")),
//                attributes,
//                "email",
//                AuthUserDetail.builder()
//                        .id(authUserId)
//                        .email("my@email.com")
//                        .profileId(12171652)
//                        .provider("google")
//                        .hasJoined(true)
//                        .isActive(true)
//                        .build());
//        //OAuth2 인증결과를 authentication 객체에 담는다.
//        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
//                principal,
//                principal.getAuthorities(),
//                "google");
//        //authentication 객체를 컨텍스트에 설정. 최종 인증 끝
//        SecurityContextHolder.getContext().setAuthentication(token);
//
//        doReturn(Integer.class).when(parameter).getParameterType();
//
//        // when
//        Object profileId = loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);
//
//        // then
//        assertThat(profileId).isNotNull();
//        assertThat(profileId).isEqualTo(principal.getAuthUserDetail().getProfileId());
//        assertThat(profileId).isInstanceOf(Integer.class);
    }
}
