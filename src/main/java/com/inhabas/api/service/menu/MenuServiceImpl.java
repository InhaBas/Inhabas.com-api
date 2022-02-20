package com.inhabas.api.service.menu;

import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.dto.menu.MenuDto;
import com.inhabas.api.dto.menu.MenuGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuGroupDto> getAllMenuInfo() {
        return menuRepository.findAllMenuByMenuGroup();
    }

    public MenuDto getMenuInfoById(Integer menuId) {

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotExistException::new);

        return new MenuDto(menuId, menu.getPriority(), menu.getName(), menu.getType(), menu.getDescription());
    }
}
