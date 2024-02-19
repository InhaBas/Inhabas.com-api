package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
@Getter
public class Phone {

  @Column(name = "PHONE", length = 15)
  private String value;

  private static final Pattern PHONE_PATTERN = Pattern.compile("^(010)-\\d{4}-\\d{4}$");

  public Phone() {}

  public Phone(String value) {
    if (this.validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;
    String o = (String) value;
    return PHONE_PATTERN.matcher(o).matches(); // only 010-****-****
  }
}
