package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntroduceTest {

  @DisplayName("자기소개란 작성")
  @Test
  public void Introduce_is_OK() {
    // given
    String introduceString = "아이엠 그라운드 자기소개 하기.";

    // when
    Introduce introduce = new Introduce(introduceString);

    // then
    assertThat(introduce.getValue()).isEqualTo("아이엠 그라운드 자기소개 하기.");
  }

  @DisplayName("자기소개가 너무 길다. 300자 이상.")
  @Test
  public void Introduce_is_too_long() {
    // given
    String introduceString = "지금이문장은10자임".repeat(50); // 500자

    // then
    assertThrows(InvalidInputException.class, () -> new Introduce(introduceString));
  }

  @DisplayName("Introduce value 가 null 이면 빈 문자열로 설정된다.")
  @Test
  public void Null_value_introduce_is_empty_string() {
    Introduce introduce = new Introduce(null);

    assertThat(introduce.getValue()).isEqualTo("");
  }
}
