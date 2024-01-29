package com.inhabas.api.auth.domain.oauth2.member.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MemberPrincipalServiceTest {

  @InjectMocks private MemberPrincipalService memberPrincipalService;

  @Mock private MemberSocialAccountRepository memberSocialAccountRepository;

  @Test
  @DisplayName("기존회원을 uid 와 provider 로 찾는다.")
  public void findMemberByUidAndProvider() {
    // given
    given(memberSocialAccountRepository.findMemberIdByUidAndProvider(any(), any()))
        .willReturn(Optional.of(1L));

    // when
    Long memberId =
        (Long)
            memberPrincipalService.loadUserPrincipal(
                new OAuth2UserInfoAuthentication("1234579123", "KAKAO", "my@gmail.com"));

    // then
    assertThat(memberId).isEqualTo(1L);
    then(memberSocialAccountRepository).should(times(1)).findMemberIdByUidAndProvider(any(), any());
    then(memberSocialAccountRepository)
        .should(times(0))
        .findMemberSocialAccountByEmailAndProvider(any(), any());
  }
}
