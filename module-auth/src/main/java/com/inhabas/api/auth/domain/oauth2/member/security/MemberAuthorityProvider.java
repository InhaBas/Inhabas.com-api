package com.inhabas.api.auth.domain.oauth2.member.security;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.SIGNING_UP;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.authException.InvalidOAuth2InfoException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;

@Component
@RequiredArgsConstructor
public class MemberAuthorityProvider implements UserAuthorityProvider {

  private final UserPrincipalService userPrincipalService;
  private final MemberRepository memberRepository;
  private static final String ROLE_PREFIX = "ROLE_";

  @Override
  @Transactional
  public Collection<SimpleGrantedAuthority> determineAuthorities(OAuth2UserInfo oAuth2UserInfo) {

    OAuth2UserInfoAuthentication authentication =
        new OAuth2UserInfoAuthentication(
            oAuth2UserInfo.getId(),
            oAuth2UserInfo.getProvider().toString(),
            oAuth2UserInfo.getEmail());
    Long memberId = (Long) userPrincipalService.loadUserPrincipal(authentication);

    if (Objects.isNull(memberId)) { // 기존회원이 아니면, member 테이블에 임시데이터 저장
      Member member =
          memberRepository
              .findByProviderAndUid(oAuth2UserInfo.getProvider(), new UID(oAuth2UserInfo.getId()))
              .orElseThrow(InvalidOAuth2InfoException::new);

      member.setRole(SIGNING_UP);

      return Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + SIGNING_UP));
    } else {
      // 기존회원이면,
      RoleDto roleDto = memberRepository.fetchRoleByStudentId(memberId);

      if (roleDto.isEmpty())
        throw new InvalidOAuth2InfoException(); // 가입된 소셜 계정으로 회원 프로필을 찾을 수 없는 경우.

      return Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + roleDto.getRole()));
    }
  }

  public static class RoleDto {
    private final Role role;

    public RoleDto(Role role) {
      this.role = role;
    }

    public Role getRole() {
      return role;
    }

    boolean isEmpty() {
      return role == null;
    }
  }
}
