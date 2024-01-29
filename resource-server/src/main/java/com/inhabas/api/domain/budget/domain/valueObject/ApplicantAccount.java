package com.inhabas.api.domain.budget.domain.valueObject;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class ApplicantAccount {

  @Column(name = "account", length = 100, nullable = false)
  private String value;

  @Transient private final int MAX_LENGTH = 100;

  public ApplicantAccount() {}

  public ApplicantAccount(String value) {
    if (validate(value)) this.value = value;
    else throw new IllegalArgumentException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return false;
    return o.length() < MAX_LENGTH;
  }

  public String getValue() {
    return value;
  }
}
