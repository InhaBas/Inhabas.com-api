package com.inhabas.api.web;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.inhabas.api.domain.menu.usecase.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메뉴 관리")
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menus")
    @SecurityRequirements(value = {})
    @Operation(summary = "모든 메뉴 정보를 가져온다.")
    @ApiResponse(responseCode = "200", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = MenuGroupDto.class)),
            examples = @ExampleObject(
                    value = "{\"id\": 1, \"groupName\": \"IBAS\", \"menuList\": [{\"menuId\": 1, \"priority\": 1, \"name\": \"동아리 소개\", \"type\": \"LIST\", \"description\": \"동아리 소개 메뉴입니다.\"}]}"
            )
    ))
    public ResponseEntity<List<MenuGroupDto>> getTotalMenuInfo() {
        List<MenuGroupDto> allMenuInfo = menuService.getAllMenuInfo();

        return ResponseEntity.ok(allMenuInfo);
    }

    @GetMapping("/menu/{menuId}")
    @Operation(summary = "id 에 해당하는 메뉴 정보를 가져온다.")
    @SecurityRequirements(value = {})
    @Parameter(
            name = "menuId",
            description = "메뉴의 고유 식별자",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = MenuDto.class),
                    examples = @ExampleObject(
                            value = "{ \"menuId\": 1, \"priority\": 1, \"name\": \"동아리 소개\", \"type\": \"LIST\", \"description\": \"동아리 소개 메뉴입니다.\" }"
                    )
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 메뉴", content = @Content(
                    examples = @ExampleObject(
                            value = "{\"message\": \"데이터가 존재하지 않습니다.\"}"
                    )
            ))
    })
    public ResponseEntity<MenuDto> getMenuInfo(@PathVariable MenuId menuId) {
        MenuDto menu = menuService.getMenuInfoById(menuId);

        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
