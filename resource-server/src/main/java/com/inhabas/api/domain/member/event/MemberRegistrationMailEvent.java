package com.inhabas.api.domain.member.event;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;

/** 입부 신청 처리 결과 메일 발송을 위한 이벤트. 트랜잭션 커밋 후 리스너에서 처리된다. */
@Getter
public class MemberRegistrationMailEvent {

  private final boolean passed;
  private final List<Receiver> receivers;

  public MemberRegistrationMailEvent(List<Member> members, boolean passed) {
    this.passed = passed;
    this.receivers =
        members.stream()
            .map(member -> new Receiver(member.getName(), member.getEmail()))
            .collect(Collectors.toList());
  }

  @Getter
  @RequiredArgsConstructor
  public static class Receiver {
    private final String name;
    private final String email;
  }
}
