package com.inhabas.api.domain.menu.dto;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Getter
public class MenuGroupDto {

  private Integer id;

  private String groupName;

  @JsonUnwrapped private List<MenuDto> menuList;

  public MenuGroupDto(Integer id, String groupName, List<MenuDto> menuList) {
    this.id = id;
    this.groupName = groupName;
    this.menuList = menuList;
  }
}
