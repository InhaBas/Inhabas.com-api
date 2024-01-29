package com.inhabas.api.domain.file.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileNameTest {

  @DisplayName("FileName 타입에 파일 이름을 저장한다.")
  @Test
  public void FileName_is_OK() {
    // when
    FileName fileName = new FileName("filename_test_file.txt");

    // then
    assertThat(fileName.getValue()).isEqualTo("filename_test_file.txt");
  }

  @DisplayName("FileName 타입에 너무 긴 파일 이름 저장을 시도한다.")
  @Test
  public void FileName_is_too_long() {
    // given
    String tooLongFileName = "a".repeat(500) + "txt";

    // when
    assertThatThrownBy(() -> new FileName(tooLongFileName))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("FileName 에 null 은 허용 안된다.")
  @Test
  public void FileName_cannot_be_null() {
    // given
    String nullFileName = null;

    // when
    assertThatThrownBy(() -> new FileName(nullFileName))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("FileName 이 빈 문자열이면 안된다.")
  @Test
  public void FileName_cannot_be_blank_string() {
    // given
    String blankFileName = " ";

    // when
    assertThatThrownBy(() -> new FileName(blankFileName))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }
}
