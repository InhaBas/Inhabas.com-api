package com.inhabas.api.domain.menu;

import com.inhabas.api.dto.menu.MenuGroupDto;
import java.util.List;

public interface MenuRepositoryCustom {
    List<MenuGroupDto> findAllMenuByMenuGroup();
}
