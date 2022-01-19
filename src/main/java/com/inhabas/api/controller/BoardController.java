package com.inhabas.api.controller;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;

import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Tag(name = "Board")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final NormalBoardRepository repository;

    @Operation(description = "게시글 조회")
    @GetMapping
    public NormalBoard board(@RequestParam Integer categoryId, @RequestParam Integer boardId) {
        return boardService.getBoard(categoryId, boardId).orElseThrow(EntityNotFoundException::new);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public Page<NormalBoard> allBoards(
            @ModelAttribute Pageable pageable,
            @RequestParam(required = false) Integer categoryId
    ) {
        Page<NormalBoard> boardList = boardService.getBoardList(pageable, categoryId);
		return boardList;
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public NormalBoard addBoard(@RequestBody SaveBoardDto saveBoardDto) {
        return boardService.write(saveBoardDto);
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public NormalBoard updateBoard(@RequestBody NormalBoard board) {
        return repository.save(board);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        repository.deleteById(id);
    }
}
