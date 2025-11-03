package com.inhabas.api.domain.menu.repository;

import static com.inhabas.api.domain.menu.domain.QMenu.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  public List<MenuGroupDto> findAllMenuByMenuGroup() {
    // Hibernate 6 호환: transform/ScrollableResults 없이 한 번에 조회 후 Java에서 그룹핑
    List<Menu> menus =
        jpaQueryFactory
            .selectFrom(menu)
            .join(menu.menuGroup)
            .fetchJoin()
            .orderBy(menu.menuGroup.id.asc(), menu.priority.asc())
            .fetch();

    // 그룹 이름 캐싱 (id -> name), 첫 등장 값 유지
    Map<Integer, String> groupNames = new LinkedHashMap<>();
    menus.forEach(
        m -> groupNames.putIfAbsent(m.getMenuGroup().getId(), m.getMenuGroup().getName()));

    // 메뉴를 그룹 ID 별로, 입력 순서 유지하며 모음
    Map<Integer, List<MenuDto>> grouped =
        menus.stream()
            .collect(
                Collectors.groupingBy(
                    m -> m.getMenuGroup().getId(),
                    LinkedHashMap::new,
                    Collectors.mapping(MenuDto::convert, Collectors.toList())));

    List<MenuGroupDto> result = new ArrayList<>(grouped.size());
    grouped.forEach(
        (groupId, menuDtos) ->
            result.add(new MenuGroupDto(groupId, groupNames.get(groupId), menuDtos)));

    return result;
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
