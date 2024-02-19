package com.inhabas.api.domain.contest.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TopicTest {

  @DisplayName("Topic을 지정한다.")
  @Test
  void validTopic() {

    String validValue = "IBAS 데이터 융·복합 서비스 구현";

    Topic topic = new Topic(validValue);

    assertThat(topic.getValue()).isEqualTo(validValue);
  }

  @DisplayName("Topic은 null일 수 없다.")
  @Test
  void nullTopicTest() {
    assertThatThrownBy(() -> new Topic(null))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Topic은 빈 값일 수 없다.")
  @Test
  void emptyTopicTest() {
    assertThatThrownBy(() -> new Topic(""))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Topic은 공백일 수 없다.")
  @Test
  void blankTopicThrowsException() {
    assertThatThrownBy(() -> new Topic("   "))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Topic은 150자를 초과할 수 없다.")
  @Test
  void exceedingMaxLengthTopicThrowsException() {

    String longValue = "a".repeat(151);

    assertThatThrownBy(() -> new Topic(longValue))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
