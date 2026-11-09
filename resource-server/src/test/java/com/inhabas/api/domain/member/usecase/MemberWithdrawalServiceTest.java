package com.inhabas.api.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.signUp.repository.AnswerRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class MemberWithdrawalServiceTest {
  @Mock private MemberRepository memberRepository;
  @Mock private MemberSocialAccountRepository socialAccountRepository;
  @Mock private AnswerRepository answerRepository;
  @InjectMocks private MemberWithdrawalService service;

  @Test
  void withdrawRemovesPersonalInformationAndSocialConnections() {
    Member member = new Member("회원", "member@example.com", Role.BASIC);
    when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

    service.withdraw(1L);

    assertThat(member.isDeleted()).isTrue();
    assertThat(member.getName()).isEqualTo("탈퇴한 회원");
    assertThat(member.getEmail()).isNotEqualTo("member@example.com");
    assertThat(member.getRole()).isEqualTo(Role.ANONYMOUS);
    assertThat(member.getProvider()).isNull();
    assertThat(member.getUid()).isNotNull();
    assertThat(member.getUid().getValue()).startsWith("withdrawn-");
    verify(socialAccountRepository).deleteByMember_IdIn(List.of(1L));
    verify(answerRepository).deleteByMember_IdIn(List.of(1L));
  }

  @Test
  void missingMemberDoesNotDeleteRelatedData() {
    when(memberRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.withdraw(1L)).isInstanceOf(MemberNotFoundException.class);
    verifyNoInteractions(socialAccountRepository, answerRepository);
  }
}
