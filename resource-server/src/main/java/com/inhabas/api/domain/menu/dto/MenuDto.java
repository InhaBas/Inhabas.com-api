package com.inhabas.api.domain.menu.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import lombok.Getter;

@Getter
public class MenuDto {

    @JsonUnwrapped
    private MenuId id;

    private Integer order;

    private String name;

    private String type;

    private String description;

    public MenuDto(MenuId id, Integer order, String name, MenuType type, String description) {
        this.id = id;
        this.order = order;
        this.name = name;
        this.type = type.toString();
        this.description = description;
    }


    public MenuDto(Integer id, Integer order, String name, MenuType type, String description) {
        this.id = new MenuId(id);
        this.order = order;
        this.name = name;
        this.type = type.toString();
        this.description = description;
    }

    public static MenuDto convert(Menu menu) {
        return new MenuDto(menu.getId(), menu.getOrder(), menu.getName(), menu.getType(), menu.getDescription());
    }
}
