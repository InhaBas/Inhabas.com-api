package com.inhabas.api.domain.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.inhabas.api.domain.menu.QMenu.menu;

import java.util.Optional;

@RequiredArgsConstructor
public class MenuRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public Optional<MenuType> findMenuTypeByMenuId(Integer id){
        return Optional.ofNullable(queryFactory.select(menu.type)
                .from(menu)
                .where(menu.id.eq(id))
                .fetchOne());
    }
}
