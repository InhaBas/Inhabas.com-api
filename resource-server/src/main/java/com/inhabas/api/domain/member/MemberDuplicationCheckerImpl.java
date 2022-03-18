package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberDuplicationCheckerImpl implements MemberDuplicationChecker {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Boolean isDuplicatedPhoneNumber(Phone phone) {
        return memberRepository.existsByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isDuplicatedId(Integer memberId) {
        return memberRepository.existsById(memberId);
    }

    @Override
    public Boolean isDuplicatedMember(Member member) {
        return memberRepository.existsByPhoneOrId(new Phone(member.getPhone()), member.getId());
    }
}
