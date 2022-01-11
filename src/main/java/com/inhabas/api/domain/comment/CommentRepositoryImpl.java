package com.inhabas.api.domain.comment;

import com.inhabas.api.domain.board.BaseBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteAllByParentBoard(BaseBoard board) {

    }

    @Override
    public List<Comment> findAllByParentBoardOrderByCreated(BaseBoard board) {
        return null;
    }
}
