package com.inhabas.api.auth.domain.oauth2.member.repository;

import java.util.Collection;
import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;

public interface MemberRepositoryCustom {

  MemberAuthorityProvider.RoleDto fetchRoleByStudentId(Long id);

  boolean isDuplicated(MemberDuplicationQueryCondition condition);

  List<Member> findAllByRoleAndStudentIdLike(Role role, String studentId);

  List<Member> findAllByRoleAndNameLike(Role role, String name);

  List<Member> findAllByRolesInAndStudentIdLike(Collection<Role> roles, String studentId);

  List<Member> findAllByRolesInAndNameLikeIncludingGraduated(
      Collection<Role> roles, String studentId);

  List<Member> findAllByRolesInAndNameLike(Collection<Role> roles, String name);

  List<Member> findAllByRolesInAndStudentIdLikeIncludingGraduated(
      Collection<Role> roles, String name);

  List<Member> findAllGraduatedByRolesInAndStudentLike(Collection<Role> roles, String studentId);

  List<Member> findAllGraduatedByRolesInAndNameLike(Collection<Role> roles, String name);
}
