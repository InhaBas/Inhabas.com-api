package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateMember(Member member) {
        log.info("member : {}", member);
        Member findMember = memberRepository.findById(member.getId());
        log.info("findMember : {}", findMember);
        if (findMember != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member findOne(Integer id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member updateMember(Member member) {
        return memberRepository.update(member);
    }
}
