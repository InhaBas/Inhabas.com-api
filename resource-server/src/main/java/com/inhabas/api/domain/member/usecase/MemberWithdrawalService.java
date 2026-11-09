package com.inhabas.api.domain.member.usecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.signUp.repository.AnswerRepository;

@Service
@RequiredArgsConstructor
public class MemberWithdrawalService {

  private final MemberRepository memberRepository;
  private final MemberSocialAccountRepository memberSocialAccountRepository;
  private final AnswerRepository answerRepository;

  @Transactional
  public void withdraw(Long memberId) {
    if (memberId == null) throw new MemberNotFoundException();
    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    if (member.isDeleted()) throw new MemberNotFoundException();
    answerRepository.deleteByMember_IdIn(List.of(memberId));
    memberSocialAccountRepository.deleteByMember_IdIn(List.of(memberId));
    member.withdraw();
  }
}
