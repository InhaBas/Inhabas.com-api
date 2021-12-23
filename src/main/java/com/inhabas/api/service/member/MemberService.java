package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;

import java.util.List;

public interface MemberService {
    public Member join(Member member);

    public List<Member> findMembers();

    public Member findOne(Integer memberId);

    public Member updateMember(Member member);
}
