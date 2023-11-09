package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;

import java.util.List;

public interface MemberRepositoryCustom {

    MemberAuthorityProvider.RoleDto fetchRoleByMemberId(StudentId studentId);

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> findByRoleAndIdLike(Role role, StudentId studentId);

    List<Member> findByRoleAndNameLike(Role role, Name name);

    List<Member> findByIdLike(StudentId studentId);
}
