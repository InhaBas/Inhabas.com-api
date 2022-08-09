package com.inhabas.api.domain.member.domain;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.ContactDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    void save(Member member);

    List<Member> findMembers();

    Member findById(MemberId memberId);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    ContactDto getChiefContact();

    void finishSignUp(Member member);
}
