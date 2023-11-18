package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.NewMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.OldMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    // 가입 관련
    void save(Member member);

    Member findById(StudentId studentId);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    void finishSignUp(Member member);


    // 회원관리 관련
    List<NewMemberManagementDto> getNewMembersBySearchAndRole(String search);

    List<OldMemberManagementDto> getOldMembersBySearchAndRole(String search);

    void updateUnapprovedMembers(List<Integer> memberIdList, String state);

    void updateApprovedMembers(List<Long> memberIdList, Role role);


    // OAuth 관련
    void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);

}
