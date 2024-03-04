package com.inhabas.api.domain.board.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import lombok.Getter;

@Getter
@Embeddable
public class Title {

  @Column(name = "TITLE", length = 100, nullable = false)
  private String value;

  @Transient private final int MAX_LENGTH = 100;

  public Title() {}

  public Title(String value) {
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

}
