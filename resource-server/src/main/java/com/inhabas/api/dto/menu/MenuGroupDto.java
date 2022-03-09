package com.inhabas.api.dto.menu;

import lombok.Getter;

import java.util.List;

@Getter
public class MenuGroupDto {

    private Integer id;

    private String groupName;

    private List<MenuDto> menuList;

    public MenuGroupDto(Integer id, String groupName, List<MenuDto> menuList) {
        this.id = id;
        this.groupName = groupName;
        this.menuList = menuList;
    }
}
