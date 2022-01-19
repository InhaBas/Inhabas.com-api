package com.inhabas.api.domain.board;

import com.inhabas.api.dto.board.BoardDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NormalBoardRepositoryImpl implements NormalBoardRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public BoardDto save(NormalBoard entity) {
        return null;
    }

    @Override
    public Optional<BoardDto> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Integer integer) {

    }

}
