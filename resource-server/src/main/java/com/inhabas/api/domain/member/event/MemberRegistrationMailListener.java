package com.inhabas.api.domain.member.event;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.inhabas.api.auth.domain.oauth2.member.service.SMTPService;

@Component
@RequiredArgsConstructor
public class MemberRegistrationMailListener {

  private static final String PASS_EMAIL_SUBJECT = "[IBAS] 축하합니다. 동아리에 입부되셨습니다.";
  private static final String FAIL_EMAIL_SUBJECT = "[IBAS] 입부 신청 결과를 알립니다.";

  private final SMTPService amazonSMTPService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(MemberRegistrationMailEvent event) {
    for (MemberRegistrationMailEvent.Receiver receiver : event.getReceivers()) {
      Map<String, Object> variables = Map.of("memberName", receiver.getName());
      if (event.isPassed()) {
        amazonSMTPService.sendPassMail(PASS_EMAIL_SUBJECT, variables, receiver.getEmail());
      } else {
        amazonSMTPService.sendRejectMail(FAIL_EMAIL_SUBJECT, variables, receiver.getEmail());
      }
    }
  }
}
