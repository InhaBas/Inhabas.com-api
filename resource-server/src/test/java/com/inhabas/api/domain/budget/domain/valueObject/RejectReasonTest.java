package com.inhabas.api.domain.budget.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RejectReasonTest {

  @DisplayName("RejectReason 타입에 문자열을 저장한다.")
  @Test
  public void normalRejectReasonTest() {
    // given
    String rejectString = "거부이유입니다.";

    // when
    RejectReason rejectReason = new RejectReason(rejectString);

    // then
    assertThat(rejectReason.getValue()).isEqualTo("거부이유입니다.");
  }

  @DisplayName("RejectReason 타입에 너무 긴 문자열을 저장한다. 200자 이상")
  @Test
  public void tooLongRejectReasonTest() {
    // given
    String rejectString = "지금이문장은10자임".repeat(20);

    // then
    assertThatThrownBy(() -> new RejectReason(rejectString))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("거부이유는 null 일 수 없습니다.")
  @Test
  public void rejectReasonCannotBeNullTest() {
    assertThatThrownBy(() -> new RejectReason(null))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("거부이유는 빈 문자열일 수 없습니다.")
  @Test
  public void rejectReasonCannotBeBlankTest() {
    assertThatThrownBy(() -> new RejectReason("\t"))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
