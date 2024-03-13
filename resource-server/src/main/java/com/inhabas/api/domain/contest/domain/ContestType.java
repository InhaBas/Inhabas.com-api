package com.inhabas.api.domain.contest.domain;

public enum ContestType {
  CONTEST("contest", 18),
  ACTIVITY("activity", 19);

  private final String boardType;
  private final int menuId;

  ContestType(String boardType, int menuId) {
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
