package com.inhabas.api.domain.contest.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContestFieldNameTest {

  @DisplayName("ContestFieldName을 지정한다.")
  @Test
  void validContestFieldName() {
    // Given
    String validValue = "빅데이터";

    // When
    ContestFieldName contestFieldName = new ContestFieldName(validValue);

    // Then
    assertThat(contestFieldName.getValue()).isEqualTo(validValue);
  }

  @DisplayName("ContestFieldName은 null일 수 없다.")
  @Test
  void nullContestFieldNameTest() {
    assertThatThrownBy(() -> new ContestFieldName(null))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("ContestFieldName은 빈 값일 수 없다.")
  @Test
  void emptyContestFieldNameTest() {
    assertThatThrownBy(() -> new ContestFieldName(""))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("ContestFieldName은 공백일 수 없다.")
  @Test
  void blankContestFieldNameTest() {
    assertThatThrownBy(() -> new ContestFieldName("   "))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("ContestFieldName은 15자를 초과할 수 없다.")
  @Test
  void exceedingMaxLengthContestFieldNameTest() {
    String longValue = "a".repeat(16);
    assertThatThrownBy(() -> new ContestFieldName(longValue))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
