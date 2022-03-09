package com.inhabas.api.domain;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuGroup;
import com.inhabas.api.domain.menu.wrapper.MenuType;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DefaultDataJpaTest
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
        MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
        Menu NoticeBoardMenu = em.persist(
                Menu.builder()
                        .menuGroup(boardMenuGroup)
                        .priority(1)
                        .type(MenuType.LIST)
                        .name("공지사항")
                        .description("부원이 알아야 할 내용을 게시합니다.")
                        .build());
        Menu freeBoardMenu = em.persist(
                Menu.builder()
                        .menuGroup(boardMenuGroup)
                        .priority(2)
                        .type(MenuType.LIST)
                        .name("자유게시판")
                        .description("부원이 자유롭게 사용할 수 있는 게시판입니다.")
                        .build());

        FREE_BOARD = NormalBoardTest.getBoard1()
                .writtenBy(saveMember).inMenu(freeBoardMenu);
        NOTICE_BOARD = NormalBoardTest.getBoard2()
                .writtenBy(saveMember).inMenu(NoticeBoardMenu);
        NOTICE_BOARD_2 = NormalBoardTest.getBoard3()
                .writtenBy(saveMember).inMenu(NoticeBoardMenu);
    }


    @DisplayName("저장 후 반환값이 처음과 같다.")
    @Test
    public void save() {
        Member saveMember = em.find(Member.class, MEMBER1.getId());

        //when
        NormalBoard saveBoard = boardRepository.save(FREE_BOARD);

        //then
        assertAll(
                () -> assertThat(saveBoard.getId()).isNotNull(),
                () -> assertThat(saveBoard.getCreated()).isNotNull(),
                () -> assertThat(saveBoard.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
                () -> assertThat(saveBoard.getContents()).isEqualTo(FREE_BOARD.getContents()),
                () -> assertThat(saveBoard.getWriter()).isEqualTo(saveMember)
        );
    }

    @DisplayName("id에 해당하는 게시글을 dto 로 반환한다.")
    @Test
    public void findDtoById() {
        //given
        boardRepository.save(FREE_BOARD);

        //when
        BoardDto find = boardRepository.findDtoById(FREE_BOARD.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertAll(
                () -> assertThat(find.getId()).isEqualTo(FREE_BOARD.getId()),
                () -> assertThat(find.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
                () -> assertThat(find.getContents()).isEqualTo(FREE_BOARD.getContents()),
                () -> assertThat(find.getMenuId()).isEqualTo(FREE_BOARD.getMenu().getId()),
                () -> assertThat(find.getWriterName()).isEqualTo(FREE_BOARD.getWriter().getName())
        );
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    public void update() {
        //given
        Member saveMember = em.find(Member.class, MEMBER1.getId());
        boardRepository.save(FREE_BOARD);

        //when
        NormalBoard param = new NormalBoard(
                FREE_BOARD.getId(), "제목이 수정되었습니다.", "내용이 수정되었습니다.").writtenBy(saveMember);
        boardRepository.save(param);

        //then
        NormalBoard findBoard = boardRepository.findById(FREE_BOARD.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertThat(findBoard.getContents()).isEqualTo("내용이 수정되었습니다.");
        assertThat(findBoard.getTitle()).isEqualTo("제목이 수정되었습니다.");
    }

    @DisplayName("id 로 게시글을 삭제한다.")
    @Test
    public void deleteById() {
        //given
        boardRepository.save(FREE_BOARD);

        //when
        boardRepository.deleteById(FREE_BOARD.getId());

        //then
        assertTrue(boardRepository.findById(FREE_BOARD.getId()).isEmpty());
    }

    @DisplayName("모든 게시글을 조회한다.")
    @Test
    public void findAll() {
        //given
        boardRepository.save(FREE_BOARD);
        boardRepository.save(NOTICE_BOARD);

        //when
        List<NormalBoard> boards = boardRepository.findAll();

        //then
        assertThat(boards).contains(FREE_BOARD, NOTICE_BOARD);
        assertThat(boards.size()).isEqualTo(2);
    }

    @DisplayName("메뉴 id 에 해당하는 게시글들을 갖고 온다.")
    @Test
    public void findAllByMenuId() {
        //given
        boardRepository.save(FREE_BOARD);
        boardRepository.save(NOTICE_BOARD);
        boardRepository.save(NOTICE_BOARD_2);
        Integer freeBoardId = FREE_BOARD.getMenu().getId();
        Integer noticeBoardId = NOTICE_BOARD.getMenu().getId();

        //when
        Page<BoardDto> freeBoards = boardRepository.findAllByMenuId(freeBoardId, Pageable.ofSize(5));
        Page<BoardDto> noticeBoards = boardRepository.findAllByMenuId(noticeBoardId, Pageable.ofSize(5));

        //then
        assertThat(freeBoards.getTotalElements()).isEqualTo(1);
        freeBoards.forEach(
                board->assertThat(board.getMenuId()).isEqualTo(freeBoardId));

        assertThat(noticeBoards.getTotalElements()).isEqualTo(2);
        noticeBoards.forEach(
                board->assertThat(board.getMenuId()).isEqualTo(noticeBoardId));
    }

}
