package com.inhabas.api.domain.budget.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.inhabas.api.domain.budget.domain.BudgetHistory;

/**
 * {@code ApplicantAccount} 와 다르게 Nullable 하다. 예산을 지원하는 경우에는 지원자에게 필수적으로 송금을 해야하는데 반해서, 총무가 예산 내역을
 * 스스로 등록하는 경우에는 송금이 이루어지지 않는 경우가 존재한다. 따라서 {@code BudgetHistory} 에서는 Nullable 한 계좌값을 사용한다.
 *
 * @see ApplicantAccount
 * @see BudgetHistory
 */
@Embeddable
public class Account {

  @Column(name = "account", length = 100)
  private String value;

  @Transient private final int MAX_LENGTH = 100;

  public Account() {}

  /**
   * String 타입이 아니거나 최대 길이를 넘으면 오류를 던짐. 어떤 문자열을 갖고 있는 경우에는 해당 문자열로 값을 설정하고, 공백 문자열로만 이루어져있으면 null 로
   * 설정.
   */
  public Account(String value) {
    if (validate(value)) this.value = ((value == null || value.isBlank()) ? null : value);
    else throw new IllegalArgumentException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return true;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return true;
    return o.length() < MAX_LENGTH;
  }

  public String getValue() {
    return value;
  }
}
