package com.inhabas.api.auth.domain.oauth2.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberFixture;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

  @InjectMocks private MemberServiceImpl memberService;

  @Mock private MemberRepository memberRepository;

  @Mock private OAuth2UserInfo oAuth2UserInfo;

  @DisplayName("회원의 권한을 변경하고 저장한다.")
  @Test
  public void changeRoleTest() {
    // given
    Member member = MemberFixture.signingUpMember1();

    // when
    memberService.changeRole(member, Role.BASIC);

    // then
    assertThat(member.getRole()).isEqualTo(Role.BASIC);
    then(memberRepository).should().save(member);
  }

  @DisplayName("회원가입을 완료하면 NOT_APPROVED 권한으로 변경된다.")
  @Test
  public void finishSignUpTest() {
    // given
    Member member = MemberFixture.signingUpMember1();

    // when
    memberService.finishSignUp(member);

    // then
    assertThat(member.getRole()).isEqualTo(Role.NOT_APPROVED);
    then(memberRepository).should().save(member);
  }

  @DisplayName("기존 소셜 계정이 있으면 마지막 로그인 시간을 갱신하여 저장한다.")
  @Test
  public void updateExistingSocialAccountTest() {
    // given
    Member member = MemberFixture.signingUpMember1();
    LocalDateTime beforeUpdate = LocalDateTime.now();

    given(oAuth2UserInfo.getProvider()).willReturn(OAuth2Provider.GOOGLE);
    given(oAuth2UserInfo.getId()).willReturn("1249846925629348");
    // Optional.orElse 는 기존 회원이 있어도 fallback Member 를 항상 생성하므로 스텁이 필요하다.
    given(oAuth2UserInfo.getEmail()).willReturn("my@gmail.com");
    given(oAuth2UserInfo.getImageUrl()).willReturn("/static/image.jpg");
    given(oAuth2UserInfo.getExtraData()).willReturn(Map.of("locale", "ko"));
    given(memberRepository.findByProviderAndUid(eq(OAuth2Provider.GOOGLE), any(UID.class)))
        .willReturn(Optional.of(member));

    // when
    memberService.updateSocialAccountInfo(oAuth2UserInfo);

    // then
    ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
    then(memberRepository).should().save(memberCaptor.capture());

    Member savedMember = memberCaptor.getValue();
    assertThat(savedMember).isSameAs(member);
    assertThat(savedMember.getLastLogin()).isAfterOrEqualTo(beforeUpdate);
  }

  @DisplayName("소셜 계정 정보가 없으면 ANONYMOUS 권한의 새로운 회원을 만들어 저장한다.")
  @Test
  public void createNewMemberFromSocialAccountTest() {
    // given
    given(oAuth2UserInfo.getProvider()).willReturn(OAuth2Provider.GOOGLE);
    given(oAuth2UserInfo.getId()).willReturn("1249846925629348");
    given(oAuth2UserInfo.getEmail()).willReturn("my@gmail.com");
    given(oAuth2UserInfo.getImageUrl()).willReturn("/static/image.jpg");
    given(oAuth2UserInfo.getExtraData()).willReturn(Map.of("locale", "ko"));
    given(memberRepository.findByProviderAndUid(eq(OAuth2Provider.GOOGLE), any(UID.class)))
        .willReturn(Optional.empty());

    // when
    memberService.updateSocialAccountInfo(oAuth2UserInfo);

    // then
    ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
    then(memberRepository).should().save(memberCaptor.capture());

    Member savedMember = memberCaptor.getValue();
    assertThat(savedMember.getRole()).isEqualTo(Role.ANONYMOUS);
    assertThat(savedMember.getEmail()).isEqualTo("my@gmail.com");
    assertThat(savedMember.getPicture()).isEqualTo("/static/image.jpg");
    assertThat(savedMember.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
    assertThat(savedMember.getLastLogin()).isNotNull();
  }
}
