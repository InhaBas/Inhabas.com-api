package com.inhabas.api.controller;

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
import java.util.Map;
import java.util.Optional;

@Slf4j
@Tag(name = "Board")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class NormalBoardController implements BoardController<Object, Object>{

    private final BoardService boardService;

    private final MenuService menuService;

    @Operation(description = "게시글 조회")
    @GetMapping
    public Object getBoard(@RequestParam Integer menuId, @RequestParam Integer id) {
        Optional<BoardController> controller = menuService.findControllerByMenuId(menuId);
        if(!controller.isEmpty())
            return controller.get().getBoard(menuId, id); // 자동으로 Object 리턴
        return boardService.getBoard(id);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public Page<Object> getBoardList(@RequestParam Integer menuId, Pageable pageable) {
        Optional<BoardController> controller = menuService.findControllerByMenuId(menuId);
        if(!controller.isEmpty())
            return controller.get().getBoardList(menuId, pageable);
        return boardService.getBoardList(menuId, pageable);
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public Integer addBoard(@RequestParam Integer menuId, @Valid @RequestBody Map<String, Object> saveBoard) {
        Optional<BoardController> controller = menuService.findControllerByMenuId(menuId);
        if(!controller.isEmpty())
            return controller.get().addBoard(menuId, saveBoard);
        return boardService.write(new SaveBoardDto(saveBoard));
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public Integer updateBoard(@RequestParam Integer menuId, @Valid @RequestBody Map<String, Object> updateBoard) {
        Optional<BoardController> controller = menuService.findControllerByMenuId(menuId);
        if(!controller.isEmpty())
            return controller.get().updateBoard(menuId, updateBoard);
        return boardService.update(new UpdateBoardDto(updateBoard));
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
    }
}