package com.inhabas.api.domain.member.event;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.service.SMTPService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MemberRegistrationMailListenerTest {

  @InjectMocks MemberRegistrationMailListener memberRegistrationMailListener;
  @Mock SMTPService amazonSMTPService;

  @DisplayName("합격 이벤트를 받으면 회원마다 합격 메일을 발송한다.")
  @Test
  void sendPassMailTest() {
    // given
    Member member = notapprovedMember();
    MemberRegistrationMailEvent event = new MemberRegistrationMailEvent(List.of(member), true);

    // when
    memberRegistrationMailListener.handle(event);

    // then
    then(amazonSMTPService)
        .should(times(1))
        .sendPassMail(anyString(), any(), eq(member.getEmail()));
    then(amazonSMTPService).should(never()).sendRejectMail(anyString(), any(), any());
  }

  @DisplayName("불합격 이벤트를 받으면 회원마다 불합격 메일을 발송한다.")
  @Test
  void sendRejectMailTest() {
    // given
    Member member = notapprovedMember();
    MemberRegistrationMailEvent event = new MemberRegistrationMailEvent(List.of(member), false);

    // when
    memberRegistrationMailListener.handle(event);

    // then
    then(amazonSMTPService)
        .should(times(1))
        .sendRejectMail(anyString(), any(), eq(member.getEmail()));
    then(amazonSMTPService).should(never()).sendPassMail(anyString(), any(), any());
  }
}
