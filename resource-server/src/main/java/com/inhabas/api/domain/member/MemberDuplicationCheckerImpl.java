package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
