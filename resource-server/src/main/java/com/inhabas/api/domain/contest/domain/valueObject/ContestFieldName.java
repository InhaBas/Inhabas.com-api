package com.inhabas.api.domain.contest.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class ContestFieldName {

  @Column(name = "NAME", length = 15, nullable = false)
  private String value;

  @Transient private final int MAX_LENGTH = 15;

  public ContestFieldName() {}

  public ContestFieldName(String value) {
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
