package com.inhabas.api.web;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "댓글 관리")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "해당 게시글의 댓글을 반환한다.")
    @GetMapping("/board/{boardId}/comments")
    public ResponseEntity<List<CommentDetailDto>> getCommentsOfBoard(
            @PathVariable(name = "boardId") Integer boardId) {

        return new ResponseEntity<>(commentService.getComments(boardId), HttpStatus.OK);
    }

    @Operation(summary = "댓글을 생성하기 위한 요청을 한다.",
            description = "parent_comment_id 값이 주어지면 대댓글, 아무값도 없으면 그냥 댓글")
    @PostMapping("/comment")
    public ResponseEntity<Integer> createNewComment(
            @Authenticated MemberId memberId,
            @Valid @RequestBody CommentSaveDto commentSaveDto) {

        Integer newCommentId = commentService.create(commentSaveDto, memberId);
        return new ResponseEntity<>(newCommentId, HttpStatus.CREATED);
    }

    @Operation(summary = "댓글을 수정하기 위한 요청을 한다.")
    @PutMapping("/comment")
    public ResponseEntity<Object> updateComment(
            @Authenticated MemberId memberId,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto) {

        commentService.update(commentUpdateDto, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "댓글 삭제 요청을 한다.")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @Authenticated MemberId memberId,
            @Positive @PathVariable Integer commentId) {

        commentService.delete(commentId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
