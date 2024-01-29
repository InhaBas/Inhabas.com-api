package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import java.util.Collection;
import java.util.List;

public interface MemberRepositoryCustom {

  MemberAuthorityProvider.RoleDto fetchRoleByStudentId(Long id);

  boolean isDuplicated(MemberDuplicationQueryCondition condition);

  List<Member> findAllByRoleAndStudentIdLike(Role role, String studentId);

  List<Member> findAllByRoleAndNameLike(Role role, String name);

  List<Member> findAllByRolesInAndStudentIdLike(Collection<Role> roles, String studentId);

  List<Member> findAllByRolesInAndNameLike(Collection<Role> roles, String name);
}
