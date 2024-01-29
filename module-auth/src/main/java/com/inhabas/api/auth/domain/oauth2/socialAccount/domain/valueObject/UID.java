package com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
public class UID {

  @Column(name = "UID", nullable = false)
  private String value;

  @Transient private final int MAX_SIZE = 255;

  public UID() {}

  public UID(String value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return false;
    return o.length() <= MAX_SIZE;
  }

  public String getValue() {
    return value;
  }
}
