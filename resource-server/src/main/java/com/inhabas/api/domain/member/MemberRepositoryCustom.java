package com.inhabas.api.domain.member;

import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;

public interface MemberRepositoryCustom {

    boolean isDuplicated(MemberDuplicationQueryCondition condition);
}
