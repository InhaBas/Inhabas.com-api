package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(
    name = "댓글 관리",
    description =
        "댓글은 게시판 종류 상관없이 같이 사용 / menuId로 CRUD 권한을 각각 확인함. 자물쇠 표기가"
            + "되어있지만 비회원이 접근 가능한 경우 토큰이 없어도 됨")
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentServiceImpl commentService;

  @Operation(summary = "해당 게시글의 댓글을 반환한다.", description = "댓글에 대한 조회 권한은 해당 게시판의 조회 권한을 따른다. ")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = CommentDetailDto.class))}),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @GetMapping("/board/{menuId}/{boardId}/comments")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_COMMENT)")
  public ResponseEntity<List<CommentDetailDto>> getCommentsOfBoard(
      @PathVariable(name = "menuId") Integer menuId, @PathVariable(name = "boardId") Long boardId) {

    return ResponseEntity.ok(commentService.getComments(menuId, boardId));
  }

  @Operation(
      summary = "댓글을 생성하기 위한 요청을 한다.",
      description =
          "parent_comment_id 값이 주어지면 대댓글, 아무값도 없으면 그냥 댓글 / 댓글에 대한 생성 권한은 해당 게시판의 생성 권한을 따른다. ")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = CommentDetailDto.class))}),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @PostMapping("/board/{menuId}/{boardId}/comment")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_COMMENT)")
  public ResponseEntity<Void> createNewComment(
      @Authenticated Long memberId,
      @PathVariable(name = "menuId") Integer menuId,
      @PathVariable(name = "boardId") Long boardId,
      @Valid @RequestBody CommentSaveDto commentSaveDto) {

    commentService.create(commentSaveDto, menuId, boardId, memberId);
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/board/{menuId}/{boardId}/comments")
            .buildAndExpand(menuId, boardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "댓글을 수정하기 위한 요청을 한다.", description = "작성자, 부회장 이상 권한을 가진 유저만 가능")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @PutMapping("/comment/{commentId}")
  @PreAuthorize("@boardSecurityChecker.commentWriterOnly(#commentId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<CommentDetailDto> updateComment(
      @Authenticated Long memberId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentUpdateDto commentUpdateDto) {

    commentService.update(commentId, commentUpdateDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "댓글 삭제 요청을 한다.", description = "작성자, 부회장 이상 권한을 가진 유저만 가능")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @DeleteMapping("/comment/{commentId}")
  @PreAuthorize("@boardSecurityChecker.commentWriterOnly(#commentId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<CommentDetailDto> deleteComment(
      @Authenticated Long memberId, @PathVariable Long commentId) {

    commentService.delete(commentId);
    return ResponseEntity.noContent().build();
  }
}
