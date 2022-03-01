package com.inhabas.api.controller;

import com.inhabas.api.domain.menu.MenuType;
import com.inhabas.api.dto.menu.MenuDto;
import com.inhabas.api.dto.menu.MenuGroupDto;
import com.inhabas.api.service.menu.MenuService;
import com.inhabas.testConfig.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(MenuController.class)
public class MenuControllerTest {

    @MockBean
    private MenuService menuService;

    @Autowired
    private MockMvc mvc;

    @DisplayName("모든 메뉴 정보를 조회한다.")
    @Test
    public void getTotalMenuInfoTest() throws Exception {
        given(menuService.getAllMenuInfo()).willReturn(
                List.of(new MenuGroupDto(1, "IBAS", List.of(new MenuDto(1,1,"동아리 소개", MenuType.INTRODUCE, "")))));

        mvc.perform(get("/menu/all"))
                .andExpect(status().isOk())
                .andReturn();

        then(menuService).should(times(1)).getAllMenuInfo();
    }

    @DisplayName("단일 메뉴 정보를 조회한다.")
    @Test
    public void getMenuInfoByIdTest() throws Exception {

        given(menuService.getMenuInfoById(anyInt())).willReturn(
                new MenuDto(6,1,"공지사항", MenuType.LIST, ""));

        mvc.perform(get("/menu")
                        .param("menuId", "6"))
                .andExpect(status().isOk())
                .andReturn();

        then(menuService).should(times(1)).getMenuInfoById(anyInt());
    }

}
