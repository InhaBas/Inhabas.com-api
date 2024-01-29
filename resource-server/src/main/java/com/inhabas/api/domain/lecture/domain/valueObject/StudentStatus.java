package com.inhabas.api.domain.lecture.domain.valueObject;

public enum StudentStatus {
  EXIT("탈주", -1),
  BLOCKED("수강 정지", 0),
  PROGRESS("진행 중", 1);

  private final Integer value;

  StudentStatus(String describe, Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
