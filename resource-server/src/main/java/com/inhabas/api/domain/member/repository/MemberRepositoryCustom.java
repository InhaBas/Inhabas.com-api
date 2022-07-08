package com.inhabas.api.domain.member.repository;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.security.MemberAuthorityProvider;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;

import java.util.List;

public interface MemberRepositoryCustom {

    MemberAuthorityProvider.RoleAndTeamDto fetchRoleAndTeamsByMemberId(MemberId memberId);

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> searchAllByRole(Role role);

    List<Member> searchByRoleLimit(Role role, Integer limit);
}
