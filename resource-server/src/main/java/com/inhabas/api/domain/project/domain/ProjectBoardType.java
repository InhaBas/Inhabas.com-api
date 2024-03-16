package com.inhabas.api.domain.project.domain;

public enum ProjectBoardType {
  ALPHA("alpha", 16),
  BETA("beta", 17);

  private final String boardType;
  private final int menuId;

  ProjectBoardType(String boardType, int menuId) {
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
