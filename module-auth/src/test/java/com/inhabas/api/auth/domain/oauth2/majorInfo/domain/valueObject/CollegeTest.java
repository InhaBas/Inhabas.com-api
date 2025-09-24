package com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollegeTest {

  @DisplayName("College 타입에 제목을 저장한다.")
  @Test
  public void saveValidCollegeName() {

    // given
    String collegeString = "사회과학대학";

    // when
    College college = new College(collegeString);

    // then
    assertThat(college.getValue()).isEqualTo("사회과학대학");
  }

  @DisplayName("College 타입에 너무 긴 이름을 저장한다. 20자 이상")
  @Test
  public void throwExceptionWhenSavingTooLongCollegeName() {

    // given
    String collegeString = "지금이문장은10자임".repeat(20);

    // then
    assertThrows(InvalidInputException.class, () -> new College(collegeString));
  }

  @DisplayName("College 은 null 일 수 없습니다.")
  @Test
  public void throwExceptionWhenSavingNullCollegeName() {

    assertThrows(InvalidInputException.class, () -> new College(null));
  }

  @DisplayName("College 은 빈 문자열일 수 없습니다.")
  @Test
  public void throwExceptionWhenSavingBlankCollegeName() {

    assertThrows(InvalidInputException.class, () -> new College("\t"));
  }
}
