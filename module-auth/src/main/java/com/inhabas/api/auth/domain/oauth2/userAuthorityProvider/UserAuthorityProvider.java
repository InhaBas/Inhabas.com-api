package com.inhabas.api.auth.domain.oauth2.userAuthorityProvider;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

/**
 * OAuth 2.0 인증이 끝난 후에, 로그인하려는 유저에게 부여할 권한을 결정한다. 아래와 같은 상황에서는, 이 인터페이스를 상속받아서 구현해야한다. <br>
 *
 * <ol>
 *   <li>기존 회원인지 아닌지를 확인하고 그에 따른 권한을 부여할 때
 *   <li>회원 등급에 따라 다른 권한을 부여하고 싶을 때
 *   <li>커스터마이징한 권한을 부여하고 싶을 때
 * </ol>
 *
 * 기본 구현체는 {@link DefaultUserAuthorityProvider}이고, 이 때에는 모든 사용자의 권한이 {@code ROLE_ANONYMOUS} 로 지정된다.
 */
public interface UserAuthorityProvider {

  Collection<SimpleGrantedAuthority> determineAuthorities(OAuth2UserInfo oAuth2UserInfo);
}
