package com.inhabas.api.domain.menu.repository;

import static com.inhabas.api.domain.menu.domain.QMenu.menu;
import static com.querydsl.core.group.GroupBy.list;

import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MenuGroupDto> findAllMenuByMenuGroup() {
        return jpaQueryFactory
                .from(menu)
                .orderBy(menu.menuGroup.id.asc())
                .orderBy(menu.order.asc())
                .transform(GroupBy.groupBy(menu.menuGroup)
                        .as(list(Projections.constructor(MenuDto.class,
                                menu.id,
                                menu.order,
                                menu.name.value,
                                menu.description.value)))
                ).entrySet().stream()
                .map(entry -> new MenuGroupDto(entry.getKey().getId(), entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Menu> findById(MenuId menuId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(menu)
                .where(menu.id.eq(Integer.parseInt(menuId.toString())))
                .fetchOne());
    }
}
