package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
public class Email {

  @Column(name = "EMAIL", length = 150, nullable = false)
  private String value;

  private static final int MAX_LENGTH = 150;

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile(
          "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

  public Email() {}

  public Email(String value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return false;
    return o.length() < MAX_LENGTH && EMAIL_PATTERN.matcher(o).matches();
  }

  public String getValue() {
    return value;
  }
}
