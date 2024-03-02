package com.inhabas.api.domain.contest.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AssociationTest {

  @DisplayName("Association을 지정한다.")
  @Test
  void validAssociation() {

    String validValue = "(주)아이바스";

    Association association = new Association(validValue);

    assertThat(association.getValue()).isEqualTo(validValue);
  }

  @DisplayName("Association은 null일 수 없다.")
  @Test
  void nullAssociationTest() {
    assertThatThrownBy(() -> new Association(null))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Association은 빈 값일 수 없다.")
  @Test
  void emptyAssociationTest() {
    assertThatThrownBy(() -> new Association(""))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Association은 공백일 수 없다.")
  @Test
  void blankAssociationTest() {
    assertThatThrownBy(() -> new Association("   "))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @Test
  void ExceedingMaxLengthAssociationTest() {
    String longValue = "a".repeat(101);
    assertThatThrownBy(() -> new Association(longValue))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
