package com.inhabas.api.domain.menu.domain;

import com.inhabas.api.domain.menu.domain.valueObject.MenuType;

public class MenuExampleTest {

  public static Menu getAlbumMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.ALBUM, "동아리 활동", "동아리 활동 설명");
  }

  public static Menu getNormalNoticeMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.NORMAL_NOTICE, "공지 사항", "공지 사항 설명");
  }

  public static Menu getBudgetHistoryMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.BUDGET_ACCOUNT, "회계 내역", "회계 내역 설명");
  }

  public static Menu getAlphaTesterMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.PROJECT, "알파 테스터", "알파테스터 설명");
  }

  public static Menu getContestMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.CONTEST, "공모전 게시판", "공모전 게시판 설명");
  }

  public static Menu getMenu2(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.STUDY, "강의", "강의 설명");
  }

  public static Menu getScholarshipMenu(MenuGroup menuGroup) {
    return new Menu(menuGroup, 1, MenuType.SPONSOR, "후원 내용", "후원 내용 설명");
  }
}
