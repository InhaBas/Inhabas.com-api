package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.dto.BoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class MemoryBoardRepository implements BoardRepository {

    private final static ConcurrentHashMap<Integer, Board> store = new ConcurrentHashMap<>();
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public MemoryBoardRepository() {
    }

    @Override
    public Board save(Board board) {
        board.setId(sequence.incrementAndGet());
        board.setCreated(new Date());
        store.put(board.getId(), board);
        return board;
    }

    @Override
    public Board findById(Integer id) {
        return store.get(id);
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Board> findByType(Category category) {
        if (category == null)
            return this.findAll();

        return store.values().stream()
                .filter(board -> board.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        store.remove(id);
    }

    @Override
    public void update(Integer id, BoardDto param) {
        Board findBoard = this.findById(id);

        if (writerIsEquals(findBoard, param)) {
            updateBoard(findBoard, param);
        } else {
            // 예외 처리..
            return;
        }
    }

    private void updateBoard(Board findBoard, BoardDto param) {
        findBoard.setContents(param.getContents());
        findBoard.setTitle(param.getTitle());
        findBoard.setUpdated(new Date());
    }

    private boolean writerIsEquals(Board findBoard, BoardDto param) {
        return Objects.equals(findBoard.getWriter(), param.getWriter());
    }

    public void clear() {
        store.clear();
    }

//    @PostConstruct
//    public void init() { // 테스트용 데이터
//        Board board1 = new Board("게시글1", "아무내용", new Member(), Category.values()[1]);
//        Board board2 = new Board("게시글2", "아무내용", new Member(), Category.values()[3]);
//
//        this.save(board1);
//        this.save(board2);
//    }
}
