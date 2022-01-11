package com.inhabas.api.domain.comment;

import com.inhabas.api.domain.board.BaseBoard;

import java.util.List;

public interface CustomCommentRepository {

    void deleteAllByParentBoard(BaseBoard board);

    List<Comment> findAllByParentBoardOrderByCreated(BaseBoard board);
}
