package com.inhabas.api.web;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "댓글 관리", description = "댓글은 게시판 종류 상관없이 같이 사용")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "해당 게시글의 댓글을 반환한다.")
    @GetMapping("/board/{menuId}/{boardId}/comments")
    public ResponseEntity<List<CommentDetailDto>> getCommentsOfBoard(
            @PathVariable(name = "menuId") Integer menuId,
            @PathVariable(name = "boardId") Long boardId) {

        return new ResponseEntity<>(commentService.getComments(menuId, boardId), HttpStatus.OK);

    }

    @Operation(summary = "댓글을 생성하기 위한 요청을 한다.",
            description = "parent_comment_id 값이 주어지면 대댓글, 아무값도 없으면 그냥 댓글")
    @PostMapping("/board/{menuId}/{boardId}/comment")
    public ResponseEntity<Long> createNewComment(
            @Authenticated Long memberId,
            @PathVariable(name = "menuId") Integer menuId,
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody CommentSaveDto commentSaveDto) {

        Long newCommentId = commentService.create(commentSaveDto, menuId, boardId, memberId);
        return new ResponseEntity<>(newCommentId, HttpStatus.CREATED);
    }

    @Operation(summary = "댓글을 수정하기 위한 요청을 한다.")
    @PutMapping("/board/{menuId}/{boardId}/comment/{commentId}")
    @PreAuthorize("@boardSecurityChecker.commentWriterOnly(#commentId) or hasRole('VICE_CHIEF')")
    public ResponseEntity<Object> updateComment(
            @Authenticated Long memberId,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto) {

        commentService.update(commentUpdateDto, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "댓글 삭제 요청을 한다.")
    @DeleteMapping("/board/{menuId}/{boardId}/comment/{commentId}")
    @PreAuthorize("@boardSecurityChecker.commentWriterOnly(#commentId) or hasRole('VICE_CHIEF')")
    public ResponseEntity<Object> deleteComment(
            @Authenticated Long memberId,
            @PathVariable(name = "commentId") Long commentId) {

        commentService.delete(commentId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
