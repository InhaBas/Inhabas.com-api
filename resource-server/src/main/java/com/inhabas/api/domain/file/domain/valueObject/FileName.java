package com.inhabas.api.domain.file.domain.valueObject;

import static com.inhabas.api.global.util.FileUtil.isValidFileName;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class FileName {

  @Column(name = "NAME")
  private String value;

  @Transient private final int MAX_LENGTH = 300;

  public FileName(String value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  public FileName() {}

  public String getValue() {
    return this.value;
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;

    String o = (String) value;
    if (o.isBlank()) return false;
    return o.length() < MAX_LENGTH && isValidFileName(o);
  }
}
