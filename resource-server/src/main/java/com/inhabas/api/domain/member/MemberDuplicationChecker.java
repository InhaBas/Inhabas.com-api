package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;

public interface MemberDuplicationChecker {

    Boolean isDuplicatedPhoneNumber(Phone phone);

    Boolean isDuplicatedId(Integer id);

    Boolean isDuplicatedMember(Member member);
}
