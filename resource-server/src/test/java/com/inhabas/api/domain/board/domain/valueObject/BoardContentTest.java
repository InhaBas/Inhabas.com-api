package com.inhabas.api.domain.board.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardContentTest {

  @DisplayName("Content 타입에 게시글 내용을 저장한다.")
  @Test
  public void Content_is_OK() {
    String contentString = ".".repeat(16777215); // 16 MB - 1

    Content content = new Content(contentString);

    assertThat(content.getValue()).isEqualTo(contentString);
  }

  @DisplayName("Content 타입에 너무 긴 게시글을 저장한다. (16 MB - 1 byte) 이상")
  @Test
  public void Contents_is_too_long() {
    String contentString = ".".repeat(16777215 + 1); // 16 MB - 1 byte

    // then
    assertThatThrownBy(() -> new Content(contentString))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Content 타입에 공백을 저장할 수 없다.")
  @Test
  public void Contents_is_Empty() {
    assertThatThrownBy(() -> new Content(""))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("Content 타입에 null 은 허용 안된다.")
  @Test
  public void Contents_is_Null() {
    assertThatThrownBy(() -> new Content(null))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
