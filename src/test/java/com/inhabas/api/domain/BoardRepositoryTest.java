package com.inhabas.api.domain;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.BoardRepository;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.BoardTest.*;
import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;


    @DisplayName("저장 후 반환값이 처음과 같다.")
    @Test
    public void save() {
        //given
        Member saveMember = memberRepository.save(MEMBER1);

        //when
        NormalBoard saveBoard = boardRepository.save(
                new NormalBoard(FREE_BOARD.getTitle(), FREE_BOARD.getContents(), saveMember, FREE_BOARD.getCategory()));

        //then
        Assertions.assertAll(
                () -> assertThat(saveBoard.getId()).isNotNull(),
                () -> assertThat(saveBoard.getCreated()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
                () -> assertThat(saveBoard.getContents()).isEqualTo(FREE_BOARD.getContents()),
                () -> assertThat(saveBoard.getCategory()).isEqualTo(FREE_BOARD.getCategory()),
                () -> assertThat(saveBoard.getWriter()).isEqualTo(saveMember)
        );
    }

    @DisplayName("id로 게시글을 찾는다.")
    @Test
    public void findById() {
        //given
        memberRepository.save(MEMBER1);
        NormalBoard saveBoard1 = boardRepository.save(FREE_BOARD);
        NormalBoard saveBoard2 = boardRepository.save(NOTICE_BOARD);

        //when
        Optional<NormalBoard> find1 = boardRepository.findById(saveBoard1.getId());
        Optional<NormalBoard> find2 = boardRepository.findById(saveBoard2.getId());

        //then
        assertThat(find1).hasValue(saveBoard1);
        assertThat(find2).hasValue(saveBoard2);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    public void update() {
        //given
        Member saveMember = memberRepository.save(MEMBER1);
        NormalBoard saveBoard = boardRepository.save(
                new NormalBoard(FREE_BOARD.getTitle(), FREE_BOARD.getContents(), saveMember, FREE_BOARD.getCategory()));

        //when
        NormalBoard param = new NormalBoard(
                saveBoard.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.", saveMember, Category.beta);
        NormalBoard updated = boardRepository.save(param);

        //then
        Optional<NormalBoard> findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(findBoard).hasValue(updated);
    }

    @DisplayName("id 로 게시글을 삭제한다.")
    @Test
    public void deleteById() {
        //given
        Member saveMember = memberRepository.save(MEMBER1);
        NormalBoard saveBoard1 = boardRepository.save(FREE_BOARD);
        NormalBoard saveBoard2 = boardRepository.save(NOTICE_BOARD);

        //when
        boardRepository.deleteById(saveBoard1.getId());

        //then
        List<NormalBoard> boards = boardRepository.findAll();

        assertThat(boards).contains(saveBoard2);
        assertThat(boards).doesNotContain(saveBoard1);
        assertThat(boards.size()).isEqualTo(1);
    }

    @DisplayName("모든 게시글을 조회한다.")
    @Test
    public void findAll() {
        //given
        memberRepository.save(MEMBER1);
        NormalBoard saveBoard1 = boardRepository.save(FREE_BOARD);
        NormalBoard saveBoard2 = boardRepository.save(NOTICE_BOARD);

        //when
        List<NormalBoard> boards = boardRepository.findAll();

        //then
        assertThat(boards).contains(saveBoard2, saveBoard1);
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    public void findAllByCategory() {
        //given
        memberRepository.save(MEMBER1);
        NormalBoard saveBoard1 = boardRepository.save(FREE_BOARD);
        NormalBoard saveBoard2 = boardRepository.save(NOTICE_BOARD);
        NormalBoard saveBoard3 = boardRepository.save(NOTICE_BOARD_2);

        //when
        List<NormalBoard> freeBoards = boardRepository.findAllByCategory(Category.free);
        List<NormalBoard> noticeBoards = boardRepository.findAllByCategory(Category.notice);

        //then
        assertThat(freeBoards).contains(saveBoard1);
        assertThat(freeBoards.size()).isEqualTo(1);
        assertThat(noticeBoards).contains(saveBoard2, saveBoard3);
        assertThat(noticeBoards.size()).isEqualTo(2);
    }

}
