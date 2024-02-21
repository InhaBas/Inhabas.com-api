package com.inhabas.api.domain.normalBoard.domain;

public enum NormalBoardType {
  NOTICE("notice", 4),
  FREE("free", 5),
  QUESTION("question", 6),
  SUGGEST("suggest", 7),
  STORAGE("storage", 8),
  EXECUTIVE("executive", 9);

  private final String boardType;
  private final int menuId;

  NormalBoardType(String boardType, int menuId) {
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
