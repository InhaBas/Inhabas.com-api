package com.inhabas.api.controller;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.repository.BoardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "UserController")
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository repository;

    @Operation(description = "게시글 조회")
    @GetMapping
    public Board board(@RequestParam Long id) {
        return repository.findById(id);
    }

    @GetMapping("/all")
    public List<Board> allBoards() {
        return repository.findAll();
    }

    @PostMapping
    public Board addBoard(@ModelAttribute Board board) {
        return repository.save(board);
    }

    @PutMapping
    public Board updateBoard(@RequestBody Board board) {
        repository.update(board.getId(), board);
        return repository.findById(board.getId());
    }

    @DeleteMapping
    public void deleteBoard(@RequestParam Long id) {
        repository.deleteById(id);
    }




}
