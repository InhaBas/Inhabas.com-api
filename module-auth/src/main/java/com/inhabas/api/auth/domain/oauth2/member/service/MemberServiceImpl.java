package com.inhabas.api.auth.domain.oauth2.member.service;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.NOT_APPROVED;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = NOT_APPROVED;
  private final MemberRepository memberRepository;

  @Transactional
  public void changeRole(Member member, Role role) {
    member.setRole(role);
    memberRepository.save(member);
  }

  @Override
  @Transactional
  public void finishSignUp(Member member) {
    member.finishSignUp();
    this.changeRole(member, DEFAULT_ROLE_AFTER_FINISH_SIGNUP);
  }

  @Override
  @Transactional
  public void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo) {

    Member member =
        memberRepository
            .findByProviderAndUid(oAuth2UserInfo.getProvider(), new UID(oAuth2UserInfo.getId()))
            .orElse(new Member(oAuth2UserInfo))
            .setLastLoginTime(LocalDateTime.now());

    memberRepository.save(member);
  }
}
