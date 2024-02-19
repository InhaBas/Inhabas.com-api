package com.inhabas.api.domain.lecture.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class StudentListDto {

  private String name;

  private Integer memberId;

  private String phoneNumber;

  private String email;

  private Integer assignmentCount;

  private Integer attendanceCount;

  private StudentStatus status;

  private Integer sid;

  @Builder
  public StudentListDto(
      String name,
      Integer memberId,
      String phoneNumber,
      String email,
      Integer assignmentCount,
      Integer attendanceCount,
      StudentStatus status,
      Integer sid) {
    this.name = name;
    this.memberId = memberId;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.assignmentCount = assignmentCount;
    this.attendanceCount = attendanceCount;
    this.status = status;
    this.sid = sid;
  }
}
