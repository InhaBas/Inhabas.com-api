package com.inhabas.api.web;

import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;
import com.inhabas.api.domain.board.usecase.BoardService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Tag(name = "Board")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(description = "게시글 조회")
    @GetMapping("/board")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<BoardDto> getBoard(@RequestParam Integer id) {
        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/boards")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 목록 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
        })
    public ResponseEntity<Page<BoardDto>> getBoardList(
            Pageable pageable,
            @RequestParam MenuId menuId) {
        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
    }

    @Operation(description = "게시글 추가")
    @PostMapping("/board")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> addBoard(@Authenticated MemberId memberId, @Valid @RequestBody SaveBoardDto saveBoardDto) {
        return new ResponseEntity<>(boardService.write(memberId, saveBoardDto), HttpStatus.CREATED);
    }

    @Operation(description = "게시글 수정")
    @PutMapping("/board")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> updateBoard(@Authenticated MemberId memberId , @Valid @RequestBody UpdateBoardDto updateBoardDto) {
        return new ResponseEntity<>(boardService.update(memberId, updateBoardDto), HttpStatus.OK);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping("/board/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 삭제 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<?> deleteBoard(@PathVariable Integer id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
