package com.inhabas.api.domain.member.domain.service;

import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.domain.member.dto.NewMemberManagementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    void save(Member member);

    Member findById(MemberId memberId);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    void finishSignUp(Member member);

    List<NewMemberManagementDto> getUnapprovedMembers(String search);

    void UpgradeUnapprovedMembers(List<Integer> memberIdList);

    List<ApprovedMemberManagementDto> getApprovedMembers(String search);

}
