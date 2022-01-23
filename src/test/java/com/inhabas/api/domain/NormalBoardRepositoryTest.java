package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.NormalBoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
public class NormalBoardRepositoryTest {

    @Autowired
    NormalBoardRepository boardRepository;
    @Autowired
    TestEntityManager em;

    NormalBoard FREE_BOARD;
    NormalBoard NOTICE_BOARD;
    NormalBoard NOTICE_BOARD_2;

    @BeforeEach
    public void setUp() {
        Member saveMember = em.persist(MEMBER1);

        FREE_BOARD = NormalBoardTest.getFreeBoard()
                .writtenBy(saveMember);
        NOTICE_BOARD = NormalBoardTest.getNoticeBoard1()
                .writtenBy(saveMember);
        NOTICE_BOARD_2 = NormalBoardTest.getNoticeBoard2()
                .writtenBy(saveMember);
    }


    @DisplayName("저장 후 반환값이 처음과 같다.")
    @Test
    public void save() {
        Member saveMember = em.find(Member.class, MEMBER1.getId());

        //when
        NormalBoard saveBoard = boardRepository.save(FREE_BOARD);

        //then
        Assertions.assertAll(
                () -> assertThat(saveBoard.getId()).isNotNull(),
                () -> assertThat(saveBoard.getCreated()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
                () -> assertThat(saveBoard.getContents()).isEqualTo(FREE_BOARD.getContents()),
                () -> assertThat(saveBoard.getWriter()).isEqualTo(saveMember)
        );
    }

    @DisplayName("id로 게시글을 찾는다.")
    @Test
    public void findById() {
        //given
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
        Member saveMember = em.find(Member.class, MEMBER1.getId());
        NormalBoard saveBoard = boardRepository.save(FREE_BOARD);

        //when
        NormalBoard param = new NormalBoard(
                saveBoard.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.").writtenBy(saveMember);
        NormalBoard updated = boardRepository.save(param);

        //then
        Optional<NormalBoard> findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(findBoard).hasValue(updated);
    }

    @DisplayName("id 로 게시글을 삭제한다.")
    @Test
    public void deleteById() {
        //given
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
        NormalBoard saveBoard1 = boardRepository.save(FREE_BOARD);
        NormalBoard saveBoard2 = boardRepository.save(NOTICE_BOARD);

        //when
        List<NormalBoard> boards = boardRepository.findAll();

        //then
        assertThat(boards).contains(saveBoard2, saveBoard1);
        assertThat(boards.size()).isEqualTo(2);
    }

}
