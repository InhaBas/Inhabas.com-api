package com.inhabas.api.board;

import com.inhabas.api.repository.MemoryBoardRepository;
import com.inhabas.api.domain.board.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryBoardRepositoryTest {

    MemoryBoardRepository store = new MemoryBoardRepository();

    @AfterEach
    void afterEach() {
        store.clear();
    }

    @Test
    void save() {
        //given
        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);

        //when
        Board savedBoard = store.save(board);

        //then
        Board findBoard = store.findById(savedBoard.getId());
        assertThat(board).isEqualTo(findBoard);
    }

    @Test
    void findAll() {
        //given
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", 12165298);

        //when
        store.save(공지사항1);
        store.save(공지사항2);

        //then
        List<Board> boards = store.findAll();
        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).contains(공지사항1, 공지사항2);
    }

    @Test
    void findById() {
        //given
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", 12165298);

        //when
        store.save(공지사항1);
        store.save(공지사항2);

        //then
        Board findBoard = store.findById(공지사항1.getId());
        assertThat(공지사항1).isEqualTo(findBoard);
    }

//    @Test
//    void update() {
//        //given
//        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);
//
//        //when
//        Board savedBoard = store.save(board);
//        Long saveId = savedBoard.getId();
//
//        //then
//        Board updateParam = new Board("공지 변경", "오늘 점심 취소입니다.", 12171652);
//        store.update(saveId, updateParam);
//
//        Board findBoard = store.findById(saveId);
//        assertThat(findBoard.getId()).isEqualTo(saveId);
//        assertThat(findBoard.getContents()).isEqualTo(updateParam.getContents());
//        assertThat(findBoard.getTitle()).isEqualTo(updateParam.getTitle());
//        assertThat(findBoard.getWriterId()).isEqualTo(updateParam.getWriterId());
//    }

//    @Test
//    void illegalUpdate() {
//        //given
//        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);
//
//        //when
//        Board savedBoard = store.save(board);
//        Long saveId = savedBoard.getId();
//
//        //then
//        Board updateParam = new Board("공지 변경", "오늘 점심 취소입니다.", 11111111); // 다른 글쓴이
//        store.update(saveId, updateParam);
//
//        Board findBoard = store.findById(saveId);
//
//        assertThat(findBoard.getId()).isEqualTo(saveId);
//        assertThat(findBoard.getContents()).isEqualTo(board.getContents());
//        assertThat(findBoard.getTitle()).isEqualTo(board.getTitle());
//        assertThat(findBoard.getWriterId()).isEqualTo(board.getWriterId());
//    }

    @Test
    void deleteById() {
        //given
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", 12171652);
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", 12165298);

        //when
        Board savedBoard = store.save(공지사항1);
        store.save(공지사항2);

        store.deleteById(savedBoard.getId());
        List<Board> boards = store.findAll();
        assertThat(boards.size()).isEqualTo(1);
        assertThat(boards).doesNotContain(공지사항1);
    }

}
