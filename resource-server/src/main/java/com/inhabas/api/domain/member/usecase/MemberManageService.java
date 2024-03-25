package com.inhabas.api.domain.member.usecase;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberSummaryDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ExecutiveMemberDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.HallOfFameDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;

public interface MemberManageService {

  List<NotApprovedMemberManagementDto> getNotApprovedMembersBySearchAndRole(String search);

  List<ApprovedMemberManagementDto> getApprovedMembersBySearchAndRole(String search);

  List<ApprovedMemberManagementDto> getGraduatedMembersBySearch(String search);

  List<ApprovedMemberSummaryDto> getAllApprovedMembersBySearchAndRole(String search);

  List<ExecutiveMemberDto> getExecutiveMembers();

  List<HallOfFameDto> getHallOfFame();

  void updateUnapprovedMembers(List<Long> memberIdList, String state);

  void updateApprovedMembersRole(List<Long> memberIdList, Role role);

  void updateApprovedMembersType(List<Long> memberIdList, MemberType type);

  ContactDto getChiefContact();
}
