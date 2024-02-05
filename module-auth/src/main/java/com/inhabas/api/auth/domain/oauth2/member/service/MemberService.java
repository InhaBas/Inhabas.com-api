package com.inhabas.api.auth.domain.oauth2.member.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

public interface MemberService {

  // 가입 관련
  void changeRole(Member member, Role role);

  void finishSignUp(Member member);

  // OAuth 관련
  void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);

}
