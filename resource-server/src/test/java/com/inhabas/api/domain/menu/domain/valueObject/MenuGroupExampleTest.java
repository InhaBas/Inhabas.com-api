package com.inhabas.api.domain.menu.domain.valueObject;

import com.inhabas.api.domain.menu.domain.MenuGroup;

public class MenuGroupExampleTest {

  public static MenuGroup getIBASMenuGroup() {
    return new MenuGroup("IBAS");
  }

  public static MenuGroup getNormalMenuGroup() {
    return new MenuGroup("게시판");
  }

  public static MenuGroup getProjectMenuGroup() {
    return new MenuGroup("프로젝트");
  }

  public static MenuGroup getBudgetMenuGroup() {
    return new MenuGroup("회계 내역");
  }

  public static MenuGroup getContestMenuGroup() {
    return new MenuGroup("공모전");
  }

  public static MenuGroup getMenuGroup2() {
    return new MenuGroup("STUDY");
  }
}
