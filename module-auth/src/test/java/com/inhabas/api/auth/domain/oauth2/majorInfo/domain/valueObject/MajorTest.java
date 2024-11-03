package com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MajorTest {

  @DisplayName("Major 타입에 제목을 저장한다.")
  @Test
  public void saveValidMajorName() {

    // given
    String majorString = "컴퓨터공학과";

    // when
    Major major = new Major(majorString);

    // then
    assertThat(major.getValue()).isEqualTo("컴퓨터공학과");
  }

  @DisplayName("Major 타입에 너무 긴 이름을 저장한다. 50자 이상")
  @Test
  public void throwExceptionWhenSavingTooLongMajorName() {

    // given
    String majorString = "지금이문장은10자임".repeat(50);

    // then
    assertThrows(InvalidInputException.class, () -> new Major(majorString));
  }

  @DisplayName("Major 은 null 일 수 없습니다.")
  @Test
  public void throwExceptionWhenSavingNullMajorName() {

    assertThrows(InvalidInputException.class, () -> new Major(null));
  }

  @DisplayName("Major 은 빈 문자열일 수 없습니다.")
  @Test
  public void throwExceptionWhenSavingBlankMajorName() {

    assertThrows(InvalidInputException.class, () -> new Major("\t"));
  }
}
