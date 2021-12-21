package com.inhabas.api.repository;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class MemoryBoardRepository implements BoardRepository {

    private final static HashMap<Long, Board> store = new HashMap<>();
    private static Long sequence = 0L;

    public MemoryBoardRepository() {
    }

    @Override
    public Board save(Board board) {
        board.setId(++sequence);
        board.setCreated(LocalDateTime.now());
        store.put(board.getId(), board);
        return board;
    }

    @Override
    public Board findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Board> findByTypes(Category type) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public void update(Long id, Board param) {
        Board findBoard = this.findById(id);
        if (Objects.equals(findBoard.getWriterId(), param.getWriterId())) {
            findBoard.setContents(param.getContents());
            findBoard.setTitle(param.getTitle());
            findBoard.setUpdated(LocalDateTime.now());
        } else {
            // 예외 처리..
            return;
        }
    }

    public void clear() {
        store.clear();
    }

    @PostConstruct
    public void init() {
        Board board1 = new Board("게시글1", "아무내용", 12171652);
        Board board2 = new Board("게시글1", "아무내용", 12171652);

        this.save(board1);
        this.save(board2);
    }
}
