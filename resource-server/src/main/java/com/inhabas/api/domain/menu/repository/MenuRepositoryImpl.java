package com.inhabas.api.domain.menu.repository;

import static com.inhabas.api.domain.menu.domain.QMenu.menu;
import static com.inhabas.api.domain.menu.domain.QMenuGroup.menuGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  public List<MenuGroupDto> findAllMenuByMenuGroup() {
    List<Menu> menus =
        jpaQueryFactory
            .selectFrom(menu)
            .leftJoin(menu.menuGroup, menuGroup)
            .fetchJoin()
            .orderBy(menu.menuGroup.id.asc(), menu.priority.asc())
            .fetch();

    // 메뉴 그룹별로 그룹화
    Map<MenuGroup, List<MenuDto>> groupedMenus = new LinkedHashMap<>();
    for (Menu m : menus) {
      MenuGroup group = m.getMenuGroup();
      groupedMenus.putIfAbsent(group, new ArrayList<>());
      groupedMenus.get(group).add(MenuDto.convert(m));
    }

    // MenuGroupDto로 변환
    return groupedMenus.entrySet().stream()
        .map(
            entry ->
                new MenuGroupDto(
                    entry.getKey().getId(), entry.getKey().getName(), entry.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Menu> findById(MenuId menuId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .selectFrom(menu)
            .where(menu.id.eq(Integer.parseInt(menuId.toString())))
            .fetchOne());
  }
}
