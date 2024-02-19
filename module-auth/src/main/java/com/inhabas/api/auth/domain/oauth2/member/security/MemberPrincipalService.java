package com.inhabas.api.auth.domain.oauth2.member.security;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Email;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.entity.MemberSocialAccount;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.auth.domain.token.securityFilter.SocialNotFoundException;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberPrincipalService implements UserPrincipalService {

  private final MemberSocialAccountRepository memberSocialAccountRepository;

  /**
   * (1) uid 와 provider 로 기존회원을 검색한다. 다만 database 레거시 호환성을 위해, 기존회원이지만 uid 가 존재하지 않는 경우를 추가로 고려해야한다.
   * 이 경우에는 (1)으로 검색되지 않는다. <br>
   * 따라서 추가로 (2) provider 와 email 로 검색한 후, 회원이 존재하면 uid 를 채워준다.
   *
   * @return StudentId
   * @exception SocialNotFoundException 최종적으로 가입되지 않은 회원이라고 판단되면 오류를 발생시킨다.
   * @see <a href="https://github.com/InhaBas/Inhabas.com/issues/102">Inhabas.com/issues/102</a>
   */
  @Transactional
  @Override
  public Object loadUserPrincipal(Authentication authentication) {

    OAuth2UserInfoAuthentication oauth2UserInfoToken =
        (OAuth2UserInfoAuthentication) authentication;
    OAuth2Provider provider = OAuth2Provider.convert(oauth2UserInfoToken.getProvider());
    UID uid = new UID(oauth2UserInfoToken.getUid());
    Email email = new Email(oauth2UserInfoToken.getEmail());

    try {
      Long memberId =
          this.getMemberId(provider, uid, email).orElseThrow(SocialNotFoundException::new);
      return memberId;
    } catch (SocialNotFoundException e) {
      log.info(e.getMessage());
      return null;
    }
  }

  private Optional<Long> getMemberId(OAuth2Provider provider, UID uid, Email email) {

    return memberSocialAccountRepository
        .findMemberIdByUidAndProvider(uid, provider)
        .or(() -> this.findByEmailAndProviderForLegacy(email, provider, uid));
  }

  private Optional<Long> findByEmailAndProviderForLegacy(
      Email email, OAuth2Provider provider, UID uid) {

    Optional<MemberSocialAccount> memberSocialAccount =
        memberSocialAccountRepository.findMemberSocialAccountByEmailAndProvider(email, provider);

    if (memberSocialAccount.isPresent()) {

      MemberSocialAccount socialAccount = memberSocialAccount.get();
      socialAccount.SetUID(uid);
      memberSocialAccountRepository.save(socialAccount);
      return Optional.of(socialAccount.getMember().getId());

    } else {

      return Optional.empty();
    }
  }
}
