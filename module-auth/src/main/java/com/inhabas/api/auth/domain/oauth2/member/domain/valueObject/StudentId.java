package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentId {

  @Column(name = "STUDENT_ID", length = 30)
  private String id;

  @Transient private static final int MAX_LENGTH = 30;

  public StudentId(String id) {
    if (validate(id)) this.id = id;
    else throw new InvalidInputException();
  }

  private boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof String)) return false;
    String o = (String) value;
    return o.length() < MAX_LENGTH;
  }

  public String getValue() {
    return id;
  }
}
