package com.inhabas.api.domain;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.BoardRepository;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    public void save_findById() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        NormalBoard board = new NormalBoard("이건 제목", "이건 내용입니다.", saveMember, Category.free);

        //when
        NormalBoard saveBoard = boardRepository.save(board);

        //then
        NormalBoard findBoard = boardRepository.findById(saveBoard.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertThat(board).isEqualTo(findBoard);
    }

    @Test
    public void update() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        NormalBoard board = new NormalBoard("이건 제목", "이건 내용입니다.", saveMember, Category.free);

        //when
        NormalBoard saveBoard = boardRepository.save(board);
        NormalBoard param = new NormalBoard(board.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.", saveMember, Category.beta);
        boardRepository.save(param);

        //then
        NormalBoard findBoard = boardRepository.findById(saveBoard.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertThat(findBoard.getContents()).isEqualTo("내용이 수정되었습니다.");
        assertThat(findBoard.getTitle()).isEqualTo("제목이 수정되었습니다.");
    }

    @Test
    public void delete() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        NormalBoard board1 = new NormalBoard("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        NormalBoard board2 = new NormalBoard("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);

        //when
        NormalBoard saveBoard1 = boardRepository.save(board1);
        NormalBoard saveBoard2 = boardRepository.save(board2);
        boardRepository.deleteById(saveBoard1.getId());

        //then
        List<NormalBoard> boards = boardRepository.findAll();

        assertThat(boards).contains(saveBoard2);
        assertThat(boards).doesNotContain(saveBoard1);
        assertThat(boards.size()).isEqualTo(1);
    }

    @Test
    public void findAll() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        NormalBoard board1 = new NormalBoard("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        NormalBoard board2 = new NormalBoard("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);

        //when
        NormalBoard saveBoard1 = boardRepository.save(board1);
        NormalBoard saveBoard2 = boardRepository.save(board2);

        //then
        List<NormalBoard> boards = boardRepository.findAll();

        assertThat(boards).contains(saveBoard2, saveBoard1);
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    public void findAllByCategory() {
        //given
        Member member = new Member(12171652, "홍길동", "010-4444-4444", null, null, new IbasInformation());
        Member saveMember = memberRepository.save(member);
        NormalBoard board1 = new NormalBoard("이건 제목", "이건 내용입니다.", saveMember, Category.free);
        NormalBoard board2 = new NormalBoard("이건 제목2", "이건 내용2입니다.", saveMember, Category.free);
        NormalBoard board3 = new NormalBoard("이건 제목3", "이건 내용3입니다.", saveMember, Category.activity);

        //when
        NormalBoard saveBoard1 = boardRepository.save(board1);
        NormalBoard saveBoard2 = boardRepository.save(board2);
        NormalBoard saveBoard3 = boardRepository.save(board3);

        //then
        List<NormalBoard> freeBoards = boardRepository.findAllByCategory(Category.free);
        List<NormalBoard> activityBoards = boardRepository.findAllByCategory(Category.activity);

        assertThat(freeBoards).contains(saveBoard1, saveBoard2);
        assertThat(freeBoards.size()).isEqualTo(2);
        assertThat(activityBoards).contains(saveBoard3);
        assertThat(activityBoards.size()).isEqualTo(1);
    }

}
