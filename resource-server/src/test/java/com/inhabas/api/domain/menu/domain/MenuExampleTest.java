package com.inhabas.api.domain.menu.domain;

import com.inhabas.api.domain.menu.domain.valueObject.MenuType;

public class MenuExampleTest {

  public static Menu getMenu1(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.ALBUM, "동아리 활동", "동아리 활동 설명");
  }

  public static Menu getMenu2(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.STUDY, "강의", "강의 설명");
  }
}
