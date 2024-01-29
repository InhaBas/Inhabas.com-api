package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Grade {

  @Column(name = "GRADE")
  private Integer value = 0;

  public Grade() {}

  public Grade(Integer value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  boolean validate(Object value) {
    if (Objects.isNull(value)) return true;
    if (!(value instanceof Integer)) return false;
    int o = (Integer) value;
    return 0 <= o && o <= 5; // 1학년부터 5학년(초과학기)까지 가능, 0학년은 학생이 아닐때
  }
}
