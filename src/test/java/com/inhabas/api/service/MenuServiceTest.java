package com.inhabas.api.service;

import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuGroup;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.domain.menu.MenuType;
import com.inhabas.api.dto.menu.MenuDto;
import com.inhabas.api.service.menu.MenuNotExistException;
import com.inhabas.api.service.menu.MenuServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    MenuServiceImpl menuService;

    @Mock
    MenuRepository menuRepository;

    @DisplayName("모든 메뉴 정보를 잘 들고오는지 테스트")
    @Test
    public void getAllMenuInfoTest() {

        given(menuRepository.findAllMenuByMenuGroup()).willReturn(new ArrayList<>());

        //when
        menuService.getAllMenuInfo();

        then(menuRepository).should(times(1)).findAllMenuByMenuGroup();
    }

    @DisplayName("한 메뉴에 대한 정보를 잘 들고오는지 테스트")
    @Test
    public void getMenuInfoByIdTest() {
        MenuGroup boardGroup = new MenuGroup("게시판");
        ReflectionTestUtils.setField(boardGroup, "id", 1);
        Menu existMenu = new Menu(boardGroup, 1, MenuType.LIST, "자유게시판", "자유로운 게시판 입니다.");
        ReflectionTestUtils.setField(existMenu, "id", 1);
        given(menuRepository.findById(anyInt())).willReturn(Optional.of(existMenu));

        //when
        MenuDto returnedMenuDto = menuService.getMenuInfoById(1);

        //then
        then(menuRepository).should(times(1)).findById(anyInt());
        assertThat(returnedMenuDto)
                .usingRecursiveComparison()
                .isEqualTo(MenuDto.convert(existMenu));
    }

    @DisplayName("존재하지 않는 메뉴를 찾으려고 하면 MenuNotExistException")
    @Test
    public void FailToGetMenuInfoByIdTest() {

        given(menuRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(MenuNotExistException.class,
                () -> menuService.getMenuInfoById(1));
    }
}
