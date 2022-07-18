package com.inhabas.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.inhabas.api.domain.menu.usecase.MenuService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
                List.of(new MenuGroupDto(1, "IBAS", List.of(new MenuDto(new MenuId(6),1,"동아리 소개", MenuType.INTRODUCE, "")))));

        mvc.perform(get("/menu/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"group_name\":\"IBAS\",\"menu_list\":[{\"menu_id\":6,\"priority\":1,\"name\":\"ë\u008F\u0099ì\u0095\u0084ë¦¬ ì\u0086\u008Cê°\u009C\",\"type\":\"INTRODUCE\",\"description\":\"\"}]}]"))
                .andReturn();

        then(menuService).should(times(1)).getAllMenuInfo();
    }

    @DisplayName("단일 메뉴 정보를 조회한다.")
    @Test
    public void getMenuInfoByIdTest() throws Exception {

        given(menuService.getMenuInfoById(any())).willReturn(
                new MenuDto(new MenuId(6),1,"공지사항", MenuType.LIST, ""));

        mvc.perform(get("/menu")
                        .param("menuId", "6"))
                .andExpect(content().string("{\"menu_id\":6,\"priority\":1,\"name\":\"ê³µì§\u0080ì\u0082¬í\u0095\u00AD\",\"type\":\"LIST\",\"description\":\"\"}"))
                .andExpect(status().isOk())
                .andReturn();

        then(menuService).should(times(1)).getMenuInfoById(any());
    }

}
