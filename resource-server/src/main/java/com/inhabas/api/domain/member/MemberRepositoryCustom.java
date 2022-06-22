package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.security.MemberAuthorityProvider;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;

import java.util.List;

public interface MemberRepositoryCustom {

    MemberAuthorityProvider.RoleAndTeamDto fetchRoleAndTeamsByMemberId(Integer memberId);

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> searchAllByRole(Role role);

    List<Member> searchByRoleLimit(Role role, Integer limit);
}
