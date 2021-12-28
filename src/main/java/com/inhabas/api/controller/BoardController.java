package com.inhabas.api.controller;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.dto.BoardDto;
import com.inhabas.api.repository.board.BoardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Board")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository repository;

    @Operation(description = "게시글 조회")
    @GetMapping
    public Board board(@RequestParam Integer id) {
        return repository.findById(id);
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public List<Board> allBoards(
            @RequestParam(required = false) Category category
    ) {
        List<Board> boardList;
		if (category == null) 
			boardList = repository.findAll();
		else
			boardList = repository.findAllByCategory(category);
		return boardList;
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public Board addBoard(@ModelAttribute Board board) {
        return repository.save(board);
    }

    @Operation(description = "게시글 수정")
    @PutMapping
    public Board updateBoard(@RequestBody Board board) {
        repository.update(board);
        return repository.findById(board.getId());
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        repository.deleteById(id);
    }
}
