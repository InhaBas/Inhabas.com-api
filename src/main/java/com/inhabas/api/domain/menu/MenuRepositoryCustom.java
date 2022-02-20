package com.inhabas.api.domain.menu;

import java.util.Optional;

public interface MenuRepositoryCustom {
    Optional<MenuType> findMenuTypeByMenuId(Integer menuId);
}
