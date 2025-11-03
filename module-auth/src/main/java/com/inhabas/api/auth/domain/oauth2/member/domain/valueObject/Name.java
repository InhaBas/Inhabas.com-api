package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
public class Name {

  @Column(name = "NAME", length = 50)
  private String value;

  @Transient private static final int MAX_LENGTH = 50;

  public Name() {}

  public Name(String value) {
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
