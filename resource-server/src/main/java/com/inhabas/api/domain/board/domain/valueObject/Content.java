package com.inhabas.api.domain.board.domain.valueObject;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
public class Content {

  @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT", nullable = false)
  private String value;

  @Transient private final int MAX_SIZE = 2 << 24 - 1; // 16MB

  public Content() {}

  public Content(String value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return false;
    return o.length() < MAX_SIZE;
  }

  public String getValue() {
    return value;
  }
}
