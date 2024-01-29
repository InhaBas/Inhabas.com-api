package com.inhabas.api.domain.menu.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;

public interface MenuRepositoryCustom {
  List<MenuGroupDto> findAllMenuByMenuGroup();

  Optional<Menu> findById(MenuId menuId);
}
