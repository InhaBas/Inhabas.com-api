package com.inhabas.api.domain.budget.domain.valueObject;

public enum ApplicationStatus {
  WAITING("승인 대기", 1),
  DENIED("승인 거절", 2),
  APPROVED("승인 완료", 3),
  PROCESSED("처리 완료", 4),
  ;

  private final Integer value;

  ApplicationStatus(String describe, Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
