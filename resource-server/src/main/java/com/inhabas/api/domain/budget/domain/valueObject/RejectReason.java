package com.inhabas.api.domain.budget.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;

@Getter
@Embeddable
public class RejectReason {

  @Column(name = "reject_reason", length = 200)
  private String value;

  @Transient private final int MAX_LENGTH = 200;

  public RejectReason() {}

  public RejectReason(String value) {
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
