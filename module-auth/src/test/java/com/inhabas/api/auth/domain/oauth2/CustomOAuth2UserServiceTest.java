package com.inhabas.api.auth.domain.oauth2;

import static com.inhabas.api.auth.testFixture.TestTimeFixture.FIXED_INSTANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestOperations;

import com.inhabas.api.auth.domain.error.authException.InvalidOAuth2InfoException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberFixture;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceTest {

  @Mock private UserAuthorityProvider userAuthorityProvider;

  @Mock private MemberService memberService;

  @Mock private MemberRepository memberRepository;

  @Mock private RestOperations restOperations;

  private CustomOAuth2UserService customOAuth2UserService;

  @BeforeEach
  public void setUp() {
    customOAuth2UserService =
        new CustomOAuth2UserService(userAuthorityProvider, memberService, memberRepository);
    customOAuth2UserService.setRestOperations(restOperations);
  }

  private static Map<String, Object> googleAttributes() {
    return new HashMap<>(
        Map.of(
            "sub", "1249846925629348",
            "name", "유동현",
            "picture", "/static/image.jpg",
            "email", "my@gmail.com",
            "locale", "ko"));
  }

  private static OAuth2UserRequest googleUserRequest() {
    ClientRegistration clientRegistration =
        ClientRegistration.withRegistrationId("google")
            .clientId("client-id")
            .clientSecret("client-secret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost/login/oauth2/code/google")
            .authorizationUri("https://example.com/oauth2/auth")
            .tokenUri("https://example.com/oauth2/token")
            .userInfoUri("https://example.com/oauth2/userinfo")
            .userNameAttributeName("sub")
            .build();
    OAuth2AccessToken accessToken =
        new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            "access-token",
            FIXED_INSTANT,
            FIXED_INSTANT.plusSeconds(3600));
    return new OAuth2UserRequest(clientRegistration, accessToken);
  }

  @SuppressWarnings("unchecked")
  private void givenUserInfoEndpointReturns(Map<String, Object> attributes) {
    given(restOperations.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
        .willReturn(ResponseEntity.ok(attributes));
  }

  @DisplayName("소셜 계정 정보를 갱신하고 회원 정보가 담긴 CustomOAuth2User 를 반환한다.")
  @Test
  public void loadUserTest() {
    // given
    givenUserInfoEndpointReturns(googleAttributes());

    Member member = MemberFixture.signingUpMember1();
    given(memberRepository.findByProviderAndUid(any(OAuth2Provider.class), any(UID.class)))
        .willReturn(Optional.of(member));
    given(userAuthorityProvider.determineAuthorities(any()))
        .willReturn(List.of(new SimpleGrantedAuthority("ROLE_SIGNING_UP")));

    // when
    OAuth2User oAuth2User = customOAuth2UserService.loadUser(googleUserRequest());

    // then
    assertThat(oAuth2User).isInstanceOf(CustomOAuth2User.class);
    CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
    assertThat(customOAuth2User.getMemberName()).isEqualTo(member.getName());
    assertThat(customOAuth2User.getMemberPicture()).isEqualTo(member.getPicture());
    assertThat(customOAuth2User.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_SIGNING_UP");
    assertThat(customOAuth2User.getAttributes().get("email")).isEqualTo("my@gmail.com");

    then(memberService).should().updateSocialAccountInfo(any(GoogleOAuth2UserInfo.class));
  }

  @DisplayName("필수값(email)이 없으면 InvalidOAuth2InfoException 을 던지고 계정 정보를 갱신하지 않는다.")
  @Test
  public void loadUserWithoutNecessaryFieldsTest() {
    // given
    Map<String, Object> attributes = googleAttributes();
    attributes.remove("email");
    givenUserInfoEndpointReturns(attributes);

    // when then
    assertThrows(
        InvalidOAuth2InfoException.class,
        () -> customOAuth2UserService.loadUser(googleUserRequest()));
    then(memberService).should(never()).updateSocialAccountInfo(any());
  }

  @DisplayName("계정 정보 갱신 후에도 회원을 찾을 수 없으면 InvalidOAuth2InfoException 을 던진다.")
  @Test
  public void loadUserMemberNotFoundTest() {
    // given
    givenUserInfoEndpointReturns(googleAttributes());
    given(memberRepository.findByProviderAndUid(any(OAuth2Provider.class), any(UID.class)))
        .willReturn(Optional.empty());

    // when then
    assertThrows(
        InvalidOAuth2InfoException.class,
        () -> customOAuth2UserService.loadUser(googleUserRequest()));
  }
}
