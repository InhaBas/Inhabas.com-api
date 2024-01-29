package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailTest {

  @DisplayName("괜찮은 이메일 목록")
  @Test
  public void createEmailTest() {
    assertDoesNotThrow(() -> new Email("gildong13-dev@naver.com"));
    assertDoesNotThrow(() -> new Email("gildong1239.1234@inha.edu"));
    assertDoesNotThrow(() -> new Email("gildong@gmail.com"));
  }

  @DisplayName("Email 타입에 이메일을 저장한다.")
  @Test
  public void Email_is_OK() {
    // when
    Email email = new Email("my@email.com");

    // then
    assertThat(email.getValue()).isEqualTo("my@email.com");
  }

  @DisplayName("Email 타입에 너무 긴 이메일 저장을 시도한다.")
  @Test
  public void Email_is_too_long() {
    // given
    String tooLongFileName = "세글자".repeat(100);

    // when
    assertThrows(InvalidInputException.class, () -> new Email(tooLongFileName));
  }

  @DisplayName("Email 에 null 은 허용 안된다.")
  @Test
  public void Email_cannot_be_null() {
    assertThrows(InvalidInputException.class, () -> new Email(null));
  }

  @DisplayName("Email 이 빈 문자열이면 안된다.")
  @Test
  public void Email_cannot_be_blank_string() {
    assertThrows(InvalidInputException.class, () -> new Email("    "));
  }

  @DisplayName("Email 이 regex.")
  @Test
  public void Email_must_be_Email_regex() {

    assertThrows(InvalidInputException.class, () -> new Email("DEV!L@email.###"));
  }
}
