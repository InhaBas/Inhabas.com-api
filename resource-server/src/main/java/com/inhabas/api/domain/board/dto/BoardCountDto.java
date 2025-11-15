package com.inhabas.api.domain.board.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.menu.domain.valueObject.MenuType;

@Getter
@NoArgsConstructor
public class BoardCountDto {

  @NotNull private Integer menuId;

  @NotNull private Integer priority;

  @NotNull private MenuType type;

  @NotNull private String menuName;

  @NotNull @PositiveOrZero private Integer count;

  @Builder
  public BoardCountDto(
      Integer menuId, Integer priority, MenuType type, String menuName, Integer count) {
    this.menuId = menuId;
    this.priority = priority;
    this.type = type;
    this.menuName = menuName;
    this.count = count;
  }
}
