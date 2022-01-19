package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.board.NormalBoardRepositoryImpl;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.dto.board.BoardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, NormalBoardRepositoryImpl.class})
public class NormalBoardRepositoryTest {

    @Autowired
    NormalBoardRepository boardRepository;
    @Autowired
    EntityManager em;

    NormalBoard FREE_BOARD;
    NormalBoard NOTICE_BOARD;
    NormalBoard NOTICE_BOARD_2;

    @BeforeEach
    public void setUp() {
        Member saveMember = MEMBER1;
        em.persist(saveMember);

        FREE_BOARD = NormalBoardTest.getFreeBoard()
                .writtenBy(saveMember)
                .inCategoryOf(em.getReference(Category.class, 2));
        NOTICE_BOARD = NormalBoardTest.getNoticeBoard1()
                .writtenBy(saveMember)
                .inCategoryOf(em.getReference(Category.class, 1));
        NOTICE_BOARD_2 = NormalBoardTest.getNoticeBoard2()
                .writtenBy(saveMember)
                .inCategoryOf(em.getReference(Category.class, 1));
    }


    @DisplayName("저장 후 반환값이 처음과 같다.")
    @Test
    public void save() {
        Member saveMember = em.find(Member.class, MEMBER1.getId());

        //when
        BoardDto saveBoard = boardRepository.save(FREE_BOARD);

        //then
        Assertions.assertAll(
                () -> assertThat(saveBoard.getId()).isNotNull(),
                () -> assertThat(saveBoard.getCreated()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
                () -> assertThat(saveBoard.getContents()).isEqualTo(FREE_BOARD.getContents()),
                () -> assertThat(saveBoard.getCategoryId()).isEqualTo(FREE_BOARD.getCategory().getId()),
                () -> assertThat(saveBoard.getWriterName()).isEqualTo(saveMember.getName())
        );
    }

    @DisplayName("id로 게시글을 찾는다.")
    @Test
    public void findById() {
        //given
        BoardDto saveBoard1 = boardRepository.save(FREE_BOARD);
        BoardDto saveBoard2 = boardRepository.save(NOTICE_BOARD);

        //when
        Optional<BoardDto> find1 = boardRepository.findById(saveBoard1.getId());
        Optional<BoardDto> find2 = boardRepository.findById(saveBoard2.getId());

        //then
        assertThat(find1).hasValue(saveBoard1);
        assertThat(find2).hasValue(saveBoard2);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    public void update() {
        //given
        Member saveMember = em.find(Member.class, MEMBER1.getId());
        BoardDto saveBoard = boardRepository.save(FREE_BOARD);

        //when
        NormalBoard param = new NormalBoard(
                saveBoard.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.").writtenBy(saveMember);
        BoardDto updated = boardRepository.save(param);

        //then
        Optional<BoardDto> findBoard = boardRepository.findById(saveBoard.getId());
        assertThat(findBoard).hasValue(updated);
    }

    @DisplayName("id 로 게시글을 삭제한다.")
    @Test
    public void deleteById() {
        //given
        BoardDto saveBoard1 = boardRepository.save(FREE_BOARD);
        Integer boardId = saveBoard1.getId();

        //when
        boardRepository.deleteById(boardId);

        //then
        Assertions.assertTrue(boardRepository.findById(boardId).isEmpty());
    }

    @DisplayName("게시판 유형에 맞게 게시글을 불러온다.")
    @Test
    public void findAllByCategory() {
        //given
        BoardDto saveBoard1 = boardRepository.save(FREE_BOARD);
        BoardDto saveBoard2 = boardRepository.save(NOTICE_BOARD);
        BoardDto saveBoard3 = boardRepository.save(NOTICE_BOARD_2);

        //when
        Page<BoardDto> freeBoards = boardRepository.findAllByCategoryId(2, Pageable.ofSize(5));
        Page<BoardDto> noticeBoards = boardRepository.findAllByCategoryId(1, Pageable.ofSize(5));

        //then
        assertThat(freeBoards).contains(saveBoard1);
        assertThat(freeBoards.getTotalElements()).isEqualTo(1);
        assertThat(noticeBoards).contains(saveBoard2, saveBoard3);
        assertThat(noticeBoards.getTotalElements()).isEqualTo(2);
    }

}
