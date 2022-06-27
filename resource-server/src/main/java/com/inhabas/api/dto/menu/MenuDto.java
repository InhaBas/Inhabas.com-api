package com.inhabas.api.dto.menu;

import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuId;
import com.inhabas.api.domain.menu.wrapper.MenuType;
import lombok.Getter;

@Getter
public class MenuDto {

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
