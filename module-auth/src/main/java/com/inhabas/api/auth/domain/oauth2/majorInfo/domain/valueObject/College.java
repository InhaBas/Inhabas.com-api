package com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
public class College {

  @Column(name = "COLLEGE", length = 20, nullable = false)
  private String value;

  @Transient private final int MAX_LENGTH = 20;

  public College() {}

  public College(String value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
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
