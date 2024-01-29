package com.inhabas.api.domain.menu.usecase;

import java.util.List;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;

public interface MenuService {

  List<MenuGroupDto> getAllMenuInfo();

  MenuDto getMenuInfoById(MenuId menuId);
}
