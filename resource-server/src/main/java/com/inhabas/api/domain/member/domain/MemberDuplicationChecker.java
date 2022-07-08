package com.inhabas.api.domain.member.domain;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;

public interface MemberDuplicationChecker {

    Boolean isDuplicatedMember(MemberDuplicationQueryCondition condition);

    Boolean isDuplicatedMember(Member member);
}
