package com.inhabas.api.auth.domain.oauth2.member.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

public interface MemberService {

  // 가입 관련
  void changeRole(Member member, Role role);

  void finishSignUp(Member member);

  // 회원관리 관련
  List<NotApprovedMemberManagementDto> getNotApprovedMembersBySearchAndRole(String search);

  List<ApprovedMemberManagementDto> getApprovedMembersBySearchAndRole(String search);

  void updateUnapprovedMembers(List<Long> memberIdList, String state);

  void updateApprovedMembers(List<Long> memberIdList, Role role);

  ContactDto getChiefContact();

  <T> List<T> getPagedDtoList(Pageable pageable, List<T> dtoList);

  // OAuth 관련
  void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);

  // 내 정보 관련
  MyProfileDto getMyProfile(Long memberId);

  void updateMyProfileDetail(Long memberId, ProfileDetailDto profileDetailDto);

  void updateMyProfileIntro(Long memberId, ProfileIntroDto profileIntroDto);

  void requestMyProfileName(Long memberId, ProfileNameDto profileNameDto);
}
