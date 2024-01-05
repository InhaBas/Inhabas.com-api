package com.inhabas.api.domain.board.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.NormalBoardTest;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@DefaultDataJpaTest
public class NormalBoardRepositoryTest {

    @Autowired
    NormalBoardRepository boardRepository;
    @Autowired
    TestEntityManager em;

    NormalBoard FREE_BOARD;
    NormalBoard NOTICE_BOARD;
    NormalBoard NOTICE_BOARD_2;
    Menu freeBoardMenu;
    Member writer;

//    @BeforeEach
//    public void setUp() {
//        writer = em.persist(basicMember1());
//        MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
//        Menu noticeBoardMenu = em.persist(
//                Menu.builder()
//                        .menuGroup(boardMenuGroup)
//                        .priority(1)
//                        .type(MenuType.LIST)
//                        .name("공지사항")
//                        .description("부원이 알아야 할 내용을 게시합니다.")
//                        .build());
//        freeBoardMenu = em.persist(
//                Menu.builder()
//                        .menuGroup(boardMenuGroup)
//                        .priority(2)
//                        .type(MenuType.LIST)
//                        .name("자유게시판")
//                        .description("부원이 자유롭게 사용할 수 있는 게시판입니다.")
//                        .build());
//
//        FREE_BOARD = NormalBoardTest.getBoard1()
//                .writtenBy(writer.getId()).inMenu(freeBoardMenu.getId());
//        NOTICE_BOARD = NormalBoardTest.getBoard2()
//                .writtenBy(writer.getId()).inMenu(noticeBoardMenu.getId());
//        NOTICE_BOARD_2 = NormalBoardTest.getBoard3()
//                .writtenBy(writer.getId()).inMenu(noticeBoardMenu.getId());
//    }


//    @DisplayName("저장 후 반환값이 처음과 같다.")
//    @Test
//    public void save() {
//        Member saveMember = em.find(Member.class, basicMember1().getId());
//
//        //when
//        NormalBoard saveBoard = boardRepository.save(FREE_BOARD);
//
//        //then
//        assertAll(
//                () -> assertThat(saveBoard.getId()).isNotNull(),
//                () -> assertThat(saveBoard.getDateCreated()).isNotNull(),
//                () -> assertThat(saveBoard.getTitle()).isEqualTo(FREE_BOARD.getTitle()),
//                () -> assertThat(saveBoard.getContent()).isEqualTo(FREE_BOARD.getContent()),
//                () -> assertThat(saveBoard.getWriterId()).isEqualTo(saveMember.getId())
//        );
//    }

//    @DisplayName("id에 해당하는 게시글을 dto 로 반환한다.")
//    @Test
//    public void findDtoById() {
//        //given
//        boardRepository.save(FREE_BOARD);
//        boardRepository.save(NOTICE_BOARD);
//
//        //when
//        BoardDto find = boardRepository.findDtoById(NOTICE_BOARD.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        //then
//        assertAll(
//                () -> assertThat(find.getId()).isEqualTo(NOTICE_BOARD.getId()),
//                () -> assertThat(find.getTitle()).isEqualTo(NOTICE_BOARD.getTitle()),
//                () -> assertThat(find.getContent()).isEqualTo(NOTICE_BOARD.getContent()),
//                () -> assertThat(find.getMenuId()).isEqualTo(NOTICE_BOARD.getMenuId()),
//                () -> assertThat(find.getWriterName()).isEqualTo(writer.getName())
//        );
//    }

//    @DisplayName("게시글을 수정한다.")
//    @Test
//    public void update() {
//        //given
//        Member saveMember = em.find(Member.class, basicMember1().getId());
//        boardRepository.save(FREE_BOARD);
//
//        //when
//        FREE_BOARD.modify("제목이 수정되었습니다.", "내용이 수정되었습니다.", saveMember.getId());
//        NormalBoard findBoard = boardRepository.findById(FREE_BOARD.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        //then
//        assertThat(findBoard.getContent()).isEqualTo("내용이 수정되었습니다.");
//        assertThat(findBoard.getTitle()).isEqualTo("제목이 수정되었습니다.");
//    }

//    @DisplayName("id 로 게시글을 삭제한다.")
//    @Test
//    public void deleteById() {
//        //given
//        boardRepository.save(FREE_BOARD);
//
//        //when
//        boardRepository.deleteById(FREE_BOARD.getId());
//
//        //then
//        assertTrue(boardRepository.findById(FREE_BOARD.getId()).isEmpty());
//    }
//
//    @DisplayName("모든 게시글을 조회한다.")
//    @Test
//    public void findAll() {
//        //given
//        boardRepository.save(FREE_BOARD);
//        boardRepository.save(NOTICE_BOARD);


//        //then
//        assertThat(boards).contains(FREE_BOARD, NOTICE_BOARD);
//        assertThat(boards.size()).isEqualTo(2);
//    }

//    @DisplayName("메뉴 id 에 해당하는 게시글들을 갖고 온다.")
//    @Test
//    public void findAllByMenuId() {
//        //given
//        boardRepository.save(FREE_BOARD);
//        boardRepository.save(NOTICE_BOARD);
//        boardRepository.save(NOTICE_BOARD_2);
//        MenuId freeBoardMenuId = FREE_BOARD.getMenuId();
//        MenuId noticeBoardMenuId = NOTICE_BOARD.getMenuId();
//
//        //when
//        Page<BoardDto> freeBoards = boardRepository.findAllByMenuId(freeBoardMenuId, Pageable.ofSize(5));
//        Page<BoardDto> noticeBoards = boardRepository.findAllByMenuId(noticeBoardMenuId, Pageable.ofSize(5));
//
//        //then
//        assertThat(freeBoards.getTotalElements()).isEqualTo(1);
//        freeBoards.forEach(
//                board->assertThat(board.getMenuId()).isEqualTo(freeBoardMenuId));
//
//        assertThat(noticeBoards.getTotalElements()).isEqualTo(2);
//        noticeBoards.forEach(
//                board->assertThat(board.getMenuId()).isEqualTo(noticeBoardMenuId));
//    }

//    @DisplayName("게시글 목록 페이지를 잘 불러온다.")
//    @Test
//    public void getBoardListPageTest() {
//        //given
//        ArrayList<NormalBoard> boards = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            boards.add(new NormalBoard("이건 제목" + i, "이건 내용입니다.")
//                    .writtenBy(writer.getId())
//                    .inMenu(freeBoardMenu.getId()));
//        }
//        boardRepository.saveAll(boards);
//
//        //when
//        Page<BoardDto> page =
//                boardRepository.findAllByMenuId(
//                        freeBoardMenu.getId(),
//                        PageRequest.of(2, 11, Direction.DESC, "created")
//                );
//
//        //then
//        assertThat(page.getTotalElements()).isEqualTo(30);
//        assertThat(page.getTotalPages()).isEqualTo(3);
//        assertThat(page.getNumber()).isEqualTo(2);
//        assertThat(page.getNumberOfElements()).isEqualTo(8);
//    }

}
