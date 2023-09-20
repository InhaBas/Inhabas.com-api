package com.inhabas.api.domain.menu.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import lombok.Getter;

/**
 * The rule of JSONProperty is located in application.yml
 */
@Getter
public class MenuDto {

    @JsonUnwrapped
    private MenuId id;

    private Integer priority;

    private String name;

    private String type;

    private String description;

    public MenuDto(MenuId id, Integer priority, String name, MenuType type, String description) {
        this.id = id;
        this.priority = priority;
        this.name = name;
        this.type = type.toString();
        this.description = description;
    }


    public MenuDto(Integer id, Integer priority, String name, MenuType type, String description) {
        this.id = new MenuId(id);
        this.priority = priority;
        this.name = name;
        this.type = type.toString();
        this.description = description;
    }

    public static MenuDto convert(Menu menu) {
        return new MenuDto(menu.getId(), menu.getPriority(), menu.getName(), menu.getType(), menu.getDescription());
    }
}
