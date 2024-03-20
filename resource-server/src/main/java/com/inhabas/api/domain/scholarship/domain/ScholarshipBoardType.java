package com.inhabas.api.domain.scholarship.domain;

public enum ScholarshipBoardType {
  SPONSOR("sponsor", 19),
  USAGE("usage", 20);

  private final String boardType;
  private final int menuId;

  ScholarshipBoardType(String boardType, int menuId) {
    this.boardType = boardType;
    this.menuId = menuId;
  }

  public String getBoardType() {
    return boardType;
  }

  public int getMenuId() {
    return menuId;
  }
}
