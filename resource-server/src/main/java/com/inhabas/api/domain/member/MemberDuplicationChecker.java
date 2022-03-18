package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;

public interface MemberDuplicationChecker {

    Boolean isDuplicatedMember(MemberDuplicationQueryCondition condition);

    Boolean isDuplicatedMember(Member member);
}
