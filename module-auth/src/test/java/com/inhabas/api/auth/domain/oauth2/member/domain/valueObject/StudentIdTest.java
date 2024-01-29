package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudentIdTest {

  @DisplayName("StudentId 타입에 유저 이름 저장")
  @Test
  public void StudentId_is_OK() {
    // given
    String id = "12171707";

    // when
    StudentId studentId = new StudentId(id);

    // then
    assertThat(studentId.getValue()).isEqualTo("12171707");
  }

  @DisplayName("StudentId 타입에 잘못된 StudentId 저장 시도. 30자 이상")
  @Test
  public void StudentId_is_too_long() {
    // given
    String id = "2023".repeat(10); // 40자

    // when
    assertThrows(InvalidInputException.class, () -> new StudentId(id));
  }

  @DisplayName("학번은 null 일 수 없다.")
  @Test
  public void StudentId_cannot_be_null() {
    assertThrows(InvalidInputException.class, () -> new StudentId(null));
  }
}
