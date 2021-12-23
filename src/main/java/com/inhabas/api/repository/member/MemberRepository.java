package com.inhabas.api.repository.member;

import com.inhabas.api.domain.member.Member;

import java.util.List;

public interface MemberRepository {

    public void save(Member member);

    public Member findById(Integer id);

    public List<Member> findAll();

    public Member update(Member member);

}
