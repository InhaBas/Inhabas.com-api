package com.inhabas.api.domain.budget.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountTest {

  @DisplayName("Account 타입에 문자열을 저장한다.")
  @Test
  public void normalAccountTest() {
    // given
    String accountString = "계좌 정보입니다.";

    // when
    Account account = new Account(accountString);

    // then
    assertThat(account.getValue()).isEqualTo("계좌 정보입니다.");
  }

  @DisplayName("Account 타입에 너무 긴 문자열을 저장한다. 100자 이상")
  @Test
  public void tooLongAccountTest() {
    // given
    String accountString = "지금이문장은10자임".repeat(10);

    // then
    assertThatThrownBy(() -> new Account(accountString))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("계좌정보는 null 이어도 됩니다.")
  @Test
  public void accountCannotBeNull() {

    // when
    Account account = new Account(null);

    // then
    assertThat(account.getValue()).isNull();
  }

  @DisplayName("계좌정보가 빈 문자열이면 null 로 변환됩니다.")
  @Test
  public void applicantAccountCannotBeNullBlank() {

    // when
    Account account = new Account("\t");

    // then
    assertThat(account.getValue()).isNull();
  }
}
