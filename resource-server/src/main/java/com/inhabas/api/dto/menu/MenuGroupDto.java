package com.inhabas.api.dto.menu;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuGroupDto {

    private Integer id;

    private String groupName;

    @JsonUnwrapped
    private List<MenuDto> menuList;

    public MenuGroupDto(Integer id, String groupName, List<MenuDto> menuList) {
        this.id = id;
        this.groupName = groupName;
        this.menuList = menuList;
    }
}
