package com.inhabas.api.domain.menu.util;

import java.util.Optional;

import com.inhabas.api.domain.contest.domain.ContestType;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.project.domain.ProjectBoardType;

public class MenuTypeToMenuIdConverter {

  // menuType을 boardType을 통해 menuId로 변환
  public static Optional<Integer> MenuTypeToMenuId(MenuType menuType) {
    Integer menuId = null;

    // NormalBoardType enum을 확인
    for (NormalBoardType boardType : NormalBoardType.values()) {
      if (boardType.getBoardType().equalsIgnoreCase(menuType.toString())) {
        return Optional.of(boardType.getMenuId());
      }
    }

    // ProjectBoardType을 확인
    for (ProjectBoardType projectBoardType : ProjectBoardType.values()) {
      if (projectBoardType.getBoardType().equalsIgnoreCase(menuType.toString())) {
        return Optional.of(projectBoardType.getMenuId());
      }
    }

    // ContestBoardType을 확인
    for (ContestType contestType : ContestType.values()) {
      if (contestType.getBoardType().equalsIgnoreCase(menuType.toString())) {
        return Optional.of(contestType.getMenuId());
      }
    }

    // 못찾으면 Optional.empty() 반환
    return Optional.empty();
  }
}
