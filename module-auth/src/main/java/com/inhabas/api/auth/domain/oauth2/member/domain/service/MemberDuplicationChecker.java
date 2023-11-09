package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;

public interface MemberDuplicationChecker {

    Boolean isDuplicatedMember(MemberDuplicationQueryCondition condition);

    Boolean isDuplicatedMember(Member member);
}
