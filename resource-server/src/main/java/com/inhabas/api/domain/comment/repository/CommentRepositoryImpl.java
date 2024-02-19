package com.inhabas.api.domain.comment.repository;

import static com.inhabas.api.domain.comment.domain.QComment.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<CommentDetailDto> findAllByParentBoardIdOrderByCreated(Long boardId) {
    List<Comment> comments =
        queryFactory
            .selectFrom(comment)
            .innerJoin(comment.writer)
            .fetchJoin()
            .leftJoin(comment.parentComment)
            .fetchJoin()
            .where(comment.parentBoard.id.eq(boardId))
            .orderBy(
                comment.dateCreated.asc(),
                comment.parentComment.id.asc().nullsFirst(),
                comment.id.asc())
            .fetch();

    return convertToNestedStructure(comments);
  }

  private List<CommentDetailDto> convertToNestedStructure(List<Comment> commentList) {

    List<CommentDetailDto> result = new ArrayList<>();
    Map<Long, CommentDetailDto> map = new HashMap<>();

    commentList.forEach(
        c -> {
          CommentDetailDto dto = CommentDetailDto.fromEntity(c);

          map.put(dto.getId(), dto);
          if (isRootComment(c)) result.add(dto);
          else map.get(c.getParentComment().getId()).getChildrenComment().add(dto);
        });

    return result;
  }

  private boolean isRootComment(Comment c) {
    return Objects.isNull(c.getParentComment());
  }
}
