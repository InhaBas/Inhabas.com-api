package com.inhabas.api.domain.budget.domain.valueObject;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

@Getter
@Embeddable
public class Price {

  @Column(nullable = false)
  private Integer value;

  public Price() {}

  public Price(Integer value) {
    if (validate(value)) this.value = value;
    else throw new InvalidInputException();
  }

  boolean validate(Object value) {
    if (Objects.isNull(value)) return false;
    if (!(value instanceof Integer)) return false;
    int o = (Integer) value;
    return 0 <= o; // 가격은 양수만 가능
  }
}
