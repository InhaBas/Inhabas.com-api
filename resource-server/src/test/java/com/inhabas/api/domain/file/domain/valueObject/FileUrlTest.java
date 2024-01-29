package com.inhabas.api.domain.file.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileUrlTest {

  @DisplayName("FileUrl 타입에 파일 경로를 저장한다.")
  @Test
  public void FileUrl_is_OK() {
    // given
    String url = "/file/board/";
    String filename = "file.txt";

    // when
    FileUrl fileURL = new FileUrl(url + filename);

    // then
    assertThat(fileURL.getValue()).isEqualTo("/file/board/file.txt");
  }

  @DisplayName("FileUrl 타입에 너무 긴 경로 저장을 시도한다.")
  @Test
  public void FileUrl_is_too_long() {
    // given
    String url = "/" + "file".repeat(300);
    String tooLongFileUrl = url + "file.txt";

    // when
    assertThatThrownBy(() -> new FileUrl(tooLongFileUrl))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("FileUrl 는 null이 될 수 없다.")
  @Test
  public void FileUrl_cannot_be_null() {
    // given
    String nullUrl = null;

    // when
    assertThatThrownBy(() -> new FileUrl(nullUrl))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("FileUrl 는 빈 문자열이 될 수 없다.")
  @Test
  public void FileUrl_cannot_be_blank() {
    // given
    String blankUrl = " ";

    // when
    assertThatThrownBy(() -> new FileUrl(blankUrl))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
