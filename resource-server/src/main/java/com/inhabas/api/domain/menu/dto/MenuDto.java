package com.inhabas.api.domain.menu.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * The rule of JSONProperty is located in application.yml
 */
@Getter
public class MenuDto {

    @JsonUnwrapped

    @Schema(description = "메뉴의 고유 식별자", example = "1")
    private MenuId id;
    @Schema(description = "메뉴의 우선순위", example = "1")
    private Integer priority;
    @Schema(description = "메뉴의 이름", example = "동아리 소개")
    private String name;
    @Schema(description = "메뉴의 타입", example = "LIST")
    private String type;
    @Schema(description = "메뉴의 설명", example = "동아리 소개 메뉴입니다.")
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
