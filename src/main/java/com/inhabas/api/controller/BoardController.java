package com.inhabas.api.controller;

import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;

import com.inhabas.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public BoardDto getBoard(@RequestParam Integer id) {
        return boardService.getBoard(id);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public Page<BoardDto> getBoardList(
            Pageable pageable,
            @RequestParam Integer menuId
    ) {
        return boardService.getBoardList(menuId, pageable);
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public Integer addBoard(@Valid @RequestBody SaveBoardDto saveBoardDto) {
        return boardService.write(saveBoardDto);
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public Integer updateBoard(@Valid @RequestBody UpdateBoardDto updateBoardDto) {
        return boardService.update(updateBoardDto);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
    }
}
