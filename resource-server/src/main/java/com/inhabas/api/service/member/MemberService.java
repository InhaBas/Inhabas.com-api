package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.member.ContactDto;

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
