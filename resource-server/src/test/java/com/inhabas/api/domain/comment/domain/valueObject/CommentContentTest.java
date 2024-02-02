package com.inhabas.api.domain.comment.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentContentTest {

  @DisplayName("Content 타입에 내용을 저장한다.")
  @Test
  public void Content_is_saved_well() {
    // given
    String contentString = "날씨 너무 좋지 않아? 개발하기 딱 좋은 날씨야! 같이 개발할래? 야 너두 할 수 있어";

    // when
    Content content = new Content(contentString);

    // then
    assertThat(content.getValue()).isEqualTo(contentString);
  }

  @DisplayName("Content 타입에 너무 긴 내용을 입력한다. 500자 이상")
  @Test
  public void Content_is_too_long() {
    // given
    String longContent = "지금이문장은10자임".repeat(50);

    // then
    assertThatThrownBy(() -> new Content(longContent))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Content 타입에 null 은 안된다.")
  @Test
  public void Content_is_Null() {
    // given
    String nullContent = null;

    // then
    assertThatThrownBy(() -> new Content(nullContent))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Content 타입에 공백댓글은 저장할 수 없다.")
  @Test
  public void Content_is_Blank() {
    // given
    String blankContent = " ";

    // then
    assertThatThrownBy(() -> new Content(blankContent))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
