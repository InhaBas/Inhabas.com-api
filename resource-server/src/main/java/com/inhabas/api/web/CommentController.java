package com.inhabas.api.web;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @Operation(description = "해당 게시글의 댓글을 반환한다.")
    @GetMapping
    public ResponseEntity<List<CommentDetailDto>> getCommentsOfBoard(
            @Positive @RequestParam(name = "boardId") Integer boardId) {

        return new ResponseEntity<>(commentService.getComments(boardId), HttpStatus.OK);
    }

    @Operation(description = "댓글을 생성하기 위한 요청을 한다.")
    @PostMapping
    public ResponseEntity<Integer> createNewComment(
            @Valid @RequestBody CommentSaveDto commentSaveDto) {

        Integer newCommentId = commentService.create(commentSaveDto);
        return new ResponseEntity<>(newCommentId, HttpStatus.CREATED);
    }

    @Operation(description = "댓글을 수정하기 위한 요청을 한다.")
    @PutMapping
    public ResponseEntity<Object> updateComment(
            @Valid @RequestBody CommentUpdateDto commentUpdateDto) {

        commentService.update(commentUpdateDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "댓글 삭제 요청을 한다.")
    @DeleteMapping
    public ResponseEntity<Object> deleteComment(
            @Positive @RequestParam Integer commentId) {

        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
