package com.inhabas.api.controller;

import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;

import com.inhabas.api.security.argumentResolver.Authenticated;
import com.inhabas.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@Tag(name = "Board")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(description = "게시글 조회")
    @GetMapping
    public ResponseEntity<BoardDto> getBoard(@RequestParam Integer id) {
        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public ResponseEntity<Page<BoardDto>> getBoardList(
            Pageable pageable,
            @RequestParam Integer menuId) {
        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public ResponseEntity<Integer> addBoard(@Authenticated Integer userId, @Valid @RequestBody SaveBoardDto saveBoardDto) {
        return new ResponseEntity<>(boardService.write(userId, saveBoardDto), HttpStatus.CREATED);
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public ResponseEntity<Integer> updateBoard(@Authenticated Integer userId ,@Valid @RequestBody UpdateBoardDto updateBoardDto) {
        return new ResponseEntity<>(boardService.update(userId, updateBoardDto), HttpStatus.OK);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
