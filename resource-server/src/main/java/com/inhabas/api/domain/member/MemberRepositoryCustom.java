package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;

import java.util.List;

public interface MemberRepositoryCustom {

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> searchAllByRole(Role role);

    List<Member> searchByRoleLimit(Role role, Integer limit);
}
