package com.inhabas.api.domain.board;

import com.inhabas.api.domain.member.QMember;
import com.inhabas.api.dto.board.BoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.inhabas.api.domain.board.QNormalBoard.normalBoard;
import static com.inhabas.api.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class NormalBoardRepositoryImpl implements NormalBoardRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        List<BoardDto> results = queryFactory.select(Projections.constructor(BoardDto.class,
                        normalBoard.id,
                        normalBoard.title.value,
                        Expressions.asString("").as("contents"),
                        normalBoard.writer.name.value,
                        normalBoard.category.id,
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(normalBoard.writer)
                .where(categoryEq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression categoryEq(Integer categoryId) {
        return normalBoard.category.id.eq(categoryId);
    }

    @Override
    public BoardDto save(NormalBoard entity) {

        Assert.notNull(entity, "Entity must not be null.");

        if (Objects.isNull(entity.getId()))
            em.persist(entity);
        else
            em.merge(entity);

        NormalBoard processedEntity = em.find(NormalBoard.class, entity.getId());

        return getBoardDto(processedEntity);
    }

    private BoardDto getBoardDto(NormalBoard processedEntity) {
        return new BoardDto(
                processedEntity.getId(),
                processedEntity.getTitle(),
                processedEntity.getContents(),
                processedEntity.getWriter().getName(),
                processedEntity.getCategory().getId(),
                processedEntity.getCreated(),
                processedEntity.getUpdated()
        );
    }

    @Override
    public Optional<BoardDto> findById(Integer id) {

        BoardDto target = queryFactory.select(Projections.constructor(BoardDto.class,
                        Expressions.asNumber(id).as("id"),
                        normalBoard.title.value,
                        normalBoard.contents.value,
                        normalBoard.writer.name.value,
                        normalBoard.category.id,
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(normalBoard.writer).on(normalBoard.id.eq(id))
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(target);
    }

    @Override
    public void deleteById(Integer id) {
        em.remove(em.getReference(NormalBoard.class, id));
    }

}
