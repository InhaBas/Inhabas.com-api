package com.inhabas.api.domain.normalBoard.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardExampleTest;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;

@DefaultDataJpaTest
public class NormalBoardRepositoryTest {

  @Autowired
  NormalBoardRepository normalBoardRepository;
  @Autowired TestEntityManager em;

  NormalBoard NOTICE_BOARD;
  NormalBoard NOTICE_BOARD_2;
  Member writer;

      @BeforeEach
      public void setUp() {
          em.getEntityManager().createNativeQuery("ALTER TABLE MENU ALTER COLUMN `id` RESTART WITH 4").executeUpdate();
          writer = em.persist(basicMember1());
          MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
          Menu noticeBoardMenu = em.persist(
                  Menu.builder()
                          .menuGroup(boardMenuGroup)
                          .priority(1)
                          .type(MenuType.NORMAL_NOTICE)
                          .name("공지사항")
                          .description("부원이 알아야 할 내용을 게시합니다.")
                          .build());
          ReflectionTestUtils.setField(noticeBoardMenu, "id", 4);
          NOTICE_BOARD = NormalBoardExampleTest.getBoard1(noticeBoardMenu)
                  .writtenBy(writer, NormalBoard.class);
          NOTICE_BOARD_2 = NormalBoardExampleTest.getBoard2(noticeBoardMenu)
                  .writtenBy(writer, NormalBoard.class);
      }

    @AfterEach
    public void deleteAll() {

        this.normalBoardRepository.deleteAll();
        this.em.clear();
    }

      @DisplayName("저장 후 반환값이 처음과 같다.")
      @Test
      public void save() {
          //when
          NormalBoard saveBoard = normalBoardRepository.save(NOTICE_BOARD);

          //then
          assertThat(saveBoard.getWriter()).isEqualTo(writer);
      }

    @DisplayName("memberId, type, search 로 게시글 목록 조회한다.")
    @Test
    public void findAllByMemberIdAndTypeAndSearch() {
        //given
        NormalBoard saveBoard = normalBoardRepository.save(NOTICE_BOARD);
        NormalBoard saveBoard2 = normalBoardRepository.save(NOTICE_BOARD_2);
        Long writerId = writer.getId();

        //when
        List<NormalBoardDto> dtoList = normalBoardRepository.findAllByMemberIdAndTypeAndSearch(writerId, NOTICE, "");

        //then
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getTitle()).isEqualTo(saveBoard.getTitle());
    }

    @DisplayName("type, search 로 게시글 목록 조회한다.")
    @Test
    public void findAllByTypeAndSearch() {
        //given
        NormalBoard saveBoard = normalBoardRepository.save(NOTICE_BOARD);
        NormalBoard saveBoard2 = normalBoardRepository.save(NOTICE_BOARD_2);

        //when
        List<NormalBoardDto> dtoList = normalBoardRepository.findAllByTypeAndSearch(NOTICE, "");

        //then
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getTitle()).isEqualTo(saveBoard.getTitle());
    }

    @DisplayName("memberId, type, id 로 게시글 상세 조회한다.")
    @Test
    public void findByMemberIdAndTypeAndId() {
        //given
        NormalBoard saveBoard = normalBoardRepository.save(NOTICE_BOARD);
        Long writerId = writer.getId();

        //when
        NormalBoard normalBoard = normalBoardRepository.findByMemberIdAndTypeAndId(writerId, NOTICE, saveBoard.getId()).orElse(null);

        //then
        assertThat(normalBoard).isNotNull();
        assertThat(normalBoard.getTitle()).isEqualTo(saveBoard.getTitle());
    }

    @DisplayName("type, id 로 게시글 상세 조회한다.")
    @Test
    public void findByTypeAndId() {
        //given
        NormalBoard saveBoard = normalBoardRepository.save(NOTICE_BOARD);

        //when
        NormalBoard normalBoard = normalBoardRepository.findByTypeAndId(NOTICE, saveBoard.getId()).orElse(null);

        //then
        assertThat(normalBoard).isNotNull();
        assertThat(normalBoard.getTitle()).isEqualTo(saveBoard.getTitle());
    }

}
