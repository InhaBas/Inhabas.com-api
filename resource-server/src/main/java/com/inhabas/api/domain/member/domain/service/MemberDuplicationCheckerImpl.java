package com.inhabas.api.domain.member.domain.service;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDuplicationCheckerImpl implements MemberDuplicationChecker {

    private final MemberRepository memberRepository;

    @Override
    public Boolean isDuplicatedMember(MemberDuplicationQueryCondition condition) {
        return memberRepository.isDuplicated(condition);
    }

    @Override
    public Boolean isDuplicatedMember(Member member) {
        return memberRepository.existsByPhoneOrId(new Phone(member.getPhone()), member.getId());
    }
}
