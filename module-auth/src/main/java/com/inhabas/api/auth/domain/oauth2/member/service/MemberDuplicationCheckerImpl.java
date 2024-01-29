package com.inhabas.api.auth.domain.oauth2.member.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;

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
