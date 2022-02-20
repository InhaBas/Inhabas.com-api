package com.inhabas.api.controller;

import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;

import com.inhabas.api.service.board.BoardService;
import com.inhabas.api.service.menu.MenuService;
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
public class NormalBoardController implements BoardController<BoardDto, BoardDto, SaveBoardDto, UpdateBoardDto>{

    private final BoardService boardService;

    private final MenuService menuService;

    @Operation(description = "게시글 조회")
    @GetMapping
    public BoardDto getBoard(@RequestParam Integer menuId, @RequestParam Integer id) {
        menuService.findControllerByMenuId(menuId).ifPresent(
                boardController -> boardController.getBoard(menuId, id));
        return boardService.getBoard(id);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public Page<BoardDto> getBoardList(
            @RequestParam Integer menuId,
            Pageable pageable) {
        menuService.findControllerByMenuId(menuId).ifPresent(
                boardController -> boardController.getBoardList(menuId, pageable));
        return boardService.getBoardList(menuId, pageable);
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public Integer addBoard(@RequestParam Integer menuId, @Valid @RequestBody SaveBoardDto saveBoardDto) {
        menuService.findControllerByMenuId(menuId).ifPresent(
                boardController -> boardController.addBoard(menuId, saveBoardDto));
        return boardService.write(saveBoardDto);
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public Integer updateBoard(@RequestParam Integer menuId, @Valid @RequestBody UpdateBoardDto updateBoardDto) {
        menuService.findControllerByMenuId(menuId).ifPresent(
                boardController -> boardController.updateBoard(menuId, updateBoardDto));
        return boardService.update(updateBoardDto);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
    }
}
