package com.inhabas.api.board.repository;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.dto.BoardDto;
import com.inhabas.api.repository.board.MemoryBoardRepository;
import com.inhabas.api.domain.board.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", new Member());

        //when
        Board savedBoard = store.save(board);

        //then
        Board findBoard = store.findById(savedBoard.getId());
        assertThat(board).isEqualTo(findBoard);
    }

    @Test
    void findAll() {
        //given
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", new Member());
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", new Member());

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
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", new Member());
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", new Member());

        //when
        store.save(공지사항1);
        store.save(공지사항2);

        //then
        Board findBoard = store.findById(공지사항1.getId());
        assertThat(공지사항1).isEqualTo(findBoard);
    }

    @Test
    void update() {
        //given
        Member writer = new Member();
        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", writer);

        //when
        Board savedBoard = store.save(board);
        Integer saveId = savedBoard.getId();

        //then
        Board updateParam = new Board(saveId, "공지 변경", "오늘 점심 취소입니다.", writer, Category.values()[3]);
        store.update(updateParam);

        Board findBoard = store.findById(saveId);
        assertThat(findBoard.getId()).isEqualTo(saveId);
        assertThat(findBoard.getContents()).isEqualTo(updateParam.getContents());
        assertThat(findBoard.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(findBoard.getWriter()).isEqualTo(updateParam.getWriter());
    }

    @Test
    void illegalUpdate() {
        //given
        Member writer1 = new Member();
        Board board = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", writer1);

        //when
        Board savedBoard = store.save(board);
        Integer saveId = savedBoard.getId();

        //then
        Member writer2 = new Member();
        Board updateParam = new Board(saveId, "공지 변경", "오늘 점심 취소입니다.", writer2, Category.values()[1]);
        store.update(updateParam);

        Board findBoard = store.findById(saveId);

        assertThat(findBoard.getId()).isEqualTo(saveId);
        assertThat(findBoard.getContents()).isEqualTo(board.getContents());
        assertThat(findBoard.getTitle()).isEqualTo(board.getTitle());
        assertThat(findBoard.getWriter()).isEqualTo(board.getWriter());
    }

    @Test
    void deleteById() {
        //given
        Board 공지사항1 = new Board("공지사항1", "오늘은 맛있는 점심을 먹습니다.", new Member());
        Board 공지사항2 = new Board("공지사항2", "오늘은 맛없는 저녁을 먹습니다.", new Member());

        //when
        Board savedBoard = store.save(공지사항1);
        store.save(공지사항2);

        store.deleteById(savedBoard.getId());
        List<Board> boards = store.findAll();
        assertThat(boards.size()).isEqualTo(1);
        assertThat(boards).doesNotContain(공지사항1);
    }

    @Test
    void findByType() {
        //given
        ArrayList<ArrayList<Board>> boards = new ArrayList<>();

        // 이차원 배열에 게시판 종류별로 개수 다르게 만듦. (ex. 자유게시판: 1개, 회장단게시판: 2개, 질문게시판: 3개 ,,,)
        for (int i = 1; i <= Category.values().length; i++) {
            ArrayList<Board> boardsTmp = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                Board board = new Board(
                        String.format("board[%d,%d]",i,j), "any title", new Member(), Category.values()[i-1]);
                boardsTmp.add(board);
                store.save(board);
            }
            boards.add(boardsTmp);
        }

        //then
        for (int i = 0; i < Category.values().length; i++) {
            int size = store.findAllByCategory(Category.values()[i]).size();
            assertThat(boards.get(i).size()).isEqualTo(size);
        }
    }

}
