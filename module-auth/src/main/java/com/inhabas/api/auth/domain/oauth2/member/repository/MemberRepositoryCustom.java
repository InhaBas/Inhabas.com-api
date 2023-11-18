package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;

import java.util.Collection;
import java.util.List;

public interface MemberRepositoryCustom {

    MemberAuthorityProvider.RoleDto fetchRoleByStudentId(StudentId studentId);

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> findByRoleAndStudentIdLike(Role role, String studentId);

    List<Member> findByRoleAndNameLike(Role role, String name);

    List<Member> findByRolesInAndStudentIdLike(Collection<Role> roles, String studentId);

    List<Member> findByRolesInAndNameLike(Collection<Role> roles, String name);

}
