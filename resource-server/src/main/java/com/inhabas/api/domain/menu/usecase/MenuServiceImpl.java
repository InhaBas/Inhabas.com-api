package com.inhabas.api.domain.menu.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;

  @Override
  @Transactional(readOnly = true)
  public List<MenuGroupDto> getAllMenuInfo() {
    return menuRepository.findAllMenuByMenuGroup();
  }

  @Override
  @Transactional(readOnly = true)
  public MenuDto getMenuInfoById(MenuId menuId) {

    Menu menu = menuRepository.findById(menuId).orElseThrow(NotFoundException::new);

    return new MenuDto(
        menuId, menu.getPriority(), menu.getName(), menu.getType(), menu.getDescription());
  }
}
