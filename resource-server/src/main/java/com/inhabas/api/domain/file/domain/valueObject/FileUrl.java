package com.inhabas.api.domain.file.domain.valueObject;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class FileUrl {

  @Column(name = "URL")
  private String value;

  @Transient private final int MAX_LENGTH = 1000;

  public FileUrl() {}

  public FileUrl(String value) {
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
    return this.value;
  }
}
