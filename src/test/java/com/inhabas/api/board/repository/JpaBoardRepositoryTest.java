package com.inhabas.api.board.repository;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.repository.board.JpaBoardRepository;
import com.inhabas.api.repository.member.JpaMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JpaBoardRepositoryTest {

    @Autowired
    JpaBoardRepository boardRepository;
    @Autowired
    JpaMemberRepository memberRepository;


    @Test
    public void save_findById() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        Board board = new Board("이건 제목", "이건 내용입니다.", saveMember, Category.free);

        //when
        Board saveBoard = boardRepository.save(board);

        //then
        Board findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(board).isEqualTo(findBoard);
    }

    @Test
    public void update() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        Board board = new Board("이건 제목", "이건 내용입니다.", saveMember, Category.free);

        //when
        Board saveBoard = boardRepository.save(board);
        Board param = new Board(board.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.",saveMember, Category.beta);
        boardRepository.update(param);

        //then
        Board findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(findBoard.getContents()).isEqualTo("내용이 수정되었습니다.");
        assertThat(findBoard.getTitle()).isEqualTo("제목이 수정되었습니다.");
    }

    @Test
    public void delete() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        Board board1 = new Board("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        Board board2 = new Board("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);

        //when
        Board saveBoard1 = boardRepository.save(board1);
        Board saveBoard2 = boardRepository.save(board2);
        boardRepository.deleteById(saveBoard1.getId());

        //then
        List<Board> boards = boardRepository.findAll();

        assertThat(boards).contains(saveBoard2);
        assertThat(boards).doesNotContain(saveBoard1);
        assertThat(boards.size()).isEqualTo(1);
    }

    @Test
    public void findAll() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        Board board1 = new Board("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        Board board2 = new Board("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);

        //when
        Board saveBoard1 = boardRepository.save(board1);
        Board saveBoard2 = boardRepository.save(board2);

        //then
        List<Board> boards = boardRepository.findAll();

        assertThat(boards).contains(saveBoard2, saveBoard1);
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    public void findAllByCategory() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        Board board1 = new Board("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        Board board2 = new Board("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);
        Board board3 = new Board("이건 제목3", "이건 내용3입니다.", saveMember, Category.activity);

        //when
        Board saveBoard1 = boardRepository.save(board1);
        Board saveBoard2 = boardRepository.save(board2);
        Board saveBoard3 = boardRepository.save(board3);

        //then
        List<Board> freeBoards = boardRepository.findAllByCategory(Category.free);
        List<Board> activityBoards = boardRepository.findAllByCategory(Category.activity);

        assertThat(freeBoards).contains(saveBoard1, saveBoard2);
        assertThat(freeBoards.size()).isEqualTo(2);
        assertThat(activityBoards).contains(saveBoard3);
        assertThat(activityBoards.size()).isEqualTo(1);
    }

}
