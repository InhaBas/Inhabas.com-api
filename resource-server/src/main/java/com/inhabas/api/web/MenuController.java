package com.inhabas.api.web;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.inhabas.api.domain.menu.usecase.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu/all")
    @Operation(summary = "모든 메뉴 정보를 가져온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MenuGroupDto>> getTotalMenuInfo() {
        List<MenuGroupDto> allMenuInfo = menuService.getAllMenuInfo();

        return ResponseEntity.ok(allMenuInfo);
    }

    @GetMapping("/menu")
    @Operation(summary = "id 에 해당하는 메뉴 정보를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 메뉴")
    })
    public ResponseEntity<MenuDto> getMenuInfo(@RequestParam MenuId menuId) {
        MenuDto menu = menuService.getMenuInfoById(menuId);

        return ResponseEntity.ok(menu);
    }
}
