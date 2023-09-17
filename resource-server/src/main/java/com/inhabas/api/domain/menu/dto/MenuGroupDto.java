package com.inhabas.api.domain.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuGroupDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("groupName")
    private String groupName;

    @JsonUnwrapped
    @JsonProperty("menuList")
    private List<MenuDto> menuList;

    public MenuGroupDto(Integer id, String groupName, List<MenuDto> menuList) {
        this.id = id;
        this.groupName = groupName;
        this.menuList = menuList;
    }
}
