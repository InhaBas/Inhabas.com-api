package com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UidTest {

  @DisplayName("UID 타입에 uid 를 저장한다.")
  @Test
  public void UID_is_OK() {
    // given
    String uidString = "1234";

    // when
    UID uid = new UID(uidString);

    // then
    assertThat(uid.getValue()).isEqualTo("1234");
  }

  @DisplayName("UID 타입에 너무 긴 제목을 저장한다. 191자 초과")
  @Test
  public void UID_is_too_long() {
    // given
    String okString = "지금이문장은10자임".repeat(19) + ".";
    String notOkString = "지금이문장은10자임".repeat(100) + "..";

    // then
    assertDoesNotThrow(() -> new UID(okString));
    assertThrows(InvalidInputException.class, () -> new UID(notOkString));
  }

  @DisplayName("UID 은 null 일 수 없습니다.")
  @Test
  public void UID_cannot_be_Null() {
    assertThrows(InvalidInputException.class, () -> new UID(null));
  }

  @DisplayName("UID 은 빈 문자열일 수 없습니다.")
  @Test
  public void UID_cannot_be_Blank() {
    assertThrows(InvalidInputException.class, () -> new UID("\t"));
  }
}
