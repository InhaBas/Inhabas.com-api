package com.inhabas.api.domain.comment.repository;

import static com.inhabas.api.domain.comment.domain.QComment.comment;

import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentDetailDto> findAllByParentBoardIdOrderByCreated(Integer boardId) {
        List<Comment> comments = queryFactory.selectFrom(comment)
                .innerJoin(comment.writer).fetchJoin()
                .leftJoin(comment.parentComment).fetchJoin()
                .where(comment.parentBoard.id.eq(boardId))
                .orderBy(comment.dateCreated.asc(), comment.parentComment.id.asc().nullsFirst(), comment.id.asc())
                .fetch();

        return convertToNestedStructure(comments);
    }

    private List<CommentDetailDto> convertToNestedStructure(List<Comment> commentList) {

        List<CommentDetailDto> result = new ArrayList<>();
        Map<Integer, CommentDetailDto> map = new HashMap<>();

        commentList.forEach(c -> {
            CommentDetailDto dto = CommentDetailDto.fromEntity(c);
            if (isRootComment(c)) {
                map.put(dto.getCommentId(), dto);
                result.add(dto);
            }
            else {
                map.get(c.getParentComment().getId()).getChildren().add(dto);
            }
        });

        return result;
    }

    private boolean isRootComment(Comment c) {
        return Objects.isNull(c.getParentComment());
    }
}
