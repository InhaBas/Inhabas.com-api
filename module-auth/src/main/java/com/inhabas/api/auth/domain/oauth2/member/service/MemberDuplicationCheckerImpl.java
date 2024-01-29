package com.inhabas.api.auth.domain.oauth2.member.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
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
    return memberRepository.existsByProviderAndUid(member.getProvider(), member.getUid());
  }
}
