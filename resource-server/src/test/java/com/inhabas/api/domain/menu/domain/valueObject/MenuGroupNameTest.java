package com.inhabas.api.domain.menu.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupNameTest {

  @DisplayName("MenuGroupName 타입에 메뉴이름 저장")
  @Test
  public void MenuGroupName_is_OK() {
    // given
    String menuGroupName = "게시판메뉴";

    // when
    MenuGroupName name = new MenuGroupName(menuGroupName);

    // then
    assertThat(name.getValue()).isEqualTo("게시판메뉴");
  }

  @DisplayName("MenuGroupName 타입에 너무 긴 메뉴이름 저장 시도. 10자 이상")
  @Test
  public void MenuGroupName_is_too_long() {
    // given
    String menuGroupName = "게시판메뉴".repeat(2); // 10자

    // when
    assertThrows(IllegalArgumentException.class, () -> new MenuGroupName(menuGroupName));
  }

  @DisplayName("메뉴그룹이름은 null 일 수 없다.")
  @Test
  public void MenuGroupName_cannot_be_null() {
    assertThrows(IllegalArgumentException.class, () -> new MenuGroupName(null));
  }

  @DisplayName("메뉴그룹이름은 빈 문자열일 수 없다.")
  @Test
  public void MenuGroupName_cannot_be_blank() {
    assertThrows(IllegalArgumentException.class, () -> new MenuGroupName(" "));
  }
}
