package com.inhabas.api.controller;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;

import com.inhabas.api.domain.board.BoardRepository;
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

    private final BoardRepository repository;

    @Operation(description = "게시글 조회")
    @GetMapping
    public NormalBoard board(@RequestParam Integer id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new); // 40x 응답할 것
    }

    @Operation(description = "모든 게시글 조회")
    @GetMapping("/all")
    public Page<NormalBoard> allBoards(
            @ModelAttribute Pageable pageable,
            @RequestParam(required = false) Category category
    ) {
        Page<NormalBoard> boardList;
		if (category == null) 
			boardList = repository.findAll(pageable);
		else
			boardList = repository.findAllByCategory(category, pageable);
		return boardList;
    }

    @Operation(description = "게시글 추가")
    @PostMapping
    public NormalBoard addBoard(@RequestBody NormalBoard board) {
        return repository.save(board);
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
