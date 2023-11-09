package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.NewMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    void save(Member member);

    Member findById(StudentId studentId);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    void finishSignUp(Member member);

    List<NewMemberManagementDto> getUnapprovedMembers(String search);

    void UpgradeUnapprovedMembers(List<Integer> memberIdList);

//    List<ApprovedMemberManagementDto> getApprovedMembers(String search);

    void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);

}
