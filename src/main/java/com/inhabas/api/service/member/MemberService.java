package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    public Optional<Member> join(Member member);

    public List<Member> findMembers();

    public Optional<Member> findOne(Integer memberId);

    public Optional<Member> updateMember(Member member);
}
