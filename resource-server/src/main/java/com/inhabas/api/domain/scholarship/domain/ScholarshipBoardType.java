package com.inhabas.api.domain.scholarship.domain;

public enum ScholarshipBoardType {
  SCHOLARSHIP("scholarship", 22),
  SPONSOR("sponsor", 20),
  USAGE("usage", 21);

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
