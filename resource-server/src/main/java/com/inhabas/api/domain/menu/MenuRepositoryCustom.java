package com.inhabas.api.domain.menu;

import com.inhabas.api.dto.menu.MenuGroupDto;
import java.util.List;
import java.util.Optional;

public interface MenuRepositoryCustom {
    List<MenuGroupDto> findAllMenuByMenuGroup();

    Optional<Menu> findById(MenuId menuId);
}
