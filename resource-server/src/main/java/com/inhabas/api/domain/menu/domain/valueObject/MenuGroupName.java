package com.inhabas.api.domain.menu.domain.valueObject;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

@Embeddable
public class MenuGroupName {
  @Column(name = "NAME", length = 10, nullable = false)
  private String value;

  @Transient private final int MAX_LENGTH = 10;

  public MenuGroupName() {}

  public MenuGroupName(String value) {
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
