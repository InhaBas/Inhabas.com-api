package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    // 가입 관련
    void save(Member member);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    void finishSignUp(Member member);


    // 회원관리 관련
    List<NotApprovedMemberManagementDto> getNotApprovedMembersBySearchAndRole(String search);

    List<ApprovedMemberManagementDto> getApprovedMembersBySearchAndRole(String search);

    void updateUnapprovedMembers(List<Integer> memberIdList, String state);

    void updateApprovedMembers(List<Long> memberIdList, Role role);

    ContactDto getChiefContact();

    List<?> getPagedDtoList(Pageable pageable, List<?> dtoList);

    // OAuth 관련
    void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);

}
