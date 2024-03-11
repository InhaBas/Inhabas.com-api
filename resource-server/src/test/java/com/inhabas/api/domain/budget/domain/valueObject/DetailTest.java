package com.inhabas.api.domain.budget.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DetailTest {

  @DisplayName("Details 타입에 문자열을 저장한다.")
  @Test
  public void normalDetailsTest() {
    // given
    String detailsString = "예산 내역 상세 내용입니다.";

    // when
    Details details = new Details(detailsString);

    // then
    assertThat(details.getValue()).isEqualTo("예산 내역 상세 내용입니다.");
  }

  @DisplayName("Details 타입에 너무 긴 문자열을 저장한다. 300자 이상")
  @Test
  public void tooLongDetailsTest() {
    // given
    String detailsString = "지금이문장은10자임".repeat(30);

    // then
    assertThrows(InvalidInputException.class, () -> new Details(detailsString));
  }

  @DisplayName("예산 내역 제목은 null 일 수 없습니다.")
  @Test
  public void detailsCannotBeNullTest() {
    assertThrows(InvalidInputException.class, () -> new Details(null));
  }

  @DisplayName("예산 상세내역 은 빈 문자열일 수 없습니다.")
  @Test
  public void detailsCannotBeBlankTest() {
    assertThrows(InvalidInputException.class, () -> new Details("\t"));
  }
}
