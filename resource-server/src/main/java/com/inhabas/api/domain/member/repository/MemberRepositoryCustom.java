package com.inhabas.api.domain.member.repository;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.Name;
import com.inhabas.api.domain.member.security.MemberAuthorityProvider;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    MemberAuthorityProvider.RoleDto fetchRoleByMemberId(MemberId memberId);

    boolean isDuplicated(MemberDuplicationQueryCondition condition);

    List<Member> findByRoleAndIdLike(Role role, MemberId memberId);

    List<Member> findByRoleAndNameLike(Role role, Name name);

    List<Member> findByIdLike(MemberId memberId);
}
