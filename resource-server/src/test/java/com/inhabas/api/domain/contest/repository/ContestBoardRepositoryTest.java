package com.inhabas.api.domain.contest.repository;

import static com.inhabas.api.domain.contest.domain.valueObject.ContestType.CONTEST;
import static com.inhabas.api.domain.contest.domain.valueObject.OrderBy.DATE_CONTEST_END;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.ContestBoardExampleTest;
import com.inhabas.api.domain.contest.domain.ContestField;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class ContestBoardRepositoryTest {

  @Autowired private ContestBoardRepository contestBoardRepository;
  @Autowired TestEntityManager em;

  ContestBoard CONTEST_BOARD;
  ContestBoard CONTEST_BOARD_2;
  Member writer;

  @BeforeEach
  public void setUp() {
    em.getEntityManager()
        .createNativeQuery("ALTER TABLE MENU ALTER COLUMN `id` RESTART WITH 18")
        .executeUpdate();
    writer = em.persist(basicMember1());
    MenuGroup contestMenuGroup = em.persist(new MenuGroup("공모전"));
    Menu contestBoardMenu =
        em.persist(
            Menu.builder()
                .menuGroup(contestMenuGroup)
                .priority(1)
                .type(MenuType.CONTEST)
                .name("공모전 게시판")
                .description("공모전 게시판 입니다.")
                .build());

    contestBoardMenu = em.persist(contestBoardMenu);

    ReflectionTestUtils.setField(contestBoardMenu, "id", 18);

    Integer contestMenuId = contestBoardMenu.getId();

    ContestField contestField = ContestField.builder().name("빅데이터").build();

    contestField = em.persist(contestField);

    CONTEST_BOARD =
        ContestBoardExampleTest.getBoard1(contestBoardMenu, contestField)
            .writtenBy(writer, ContestBoard.class);
    CONTEST_BOARD_2 =
        ContestBoardExampleTest.getBoard2(contestBoardMenu, contestField)
            .writtenBy(writer, ContestBoard.class);
  }

  @AfterEach
  public void deleteAll() {

    this.contestBoardRepository.deleteAll();
    this.em.clear();
  }

  @DisplayName("저장 후 반환값이 처음과 같다.")
  @Test
  public void save() {
    // when
    ContestBoard saveBoard = contestBoardRepository.save(CONTEST_BOARD);

    // then
    assertThat(saveBoard.getWriter()).isEqualTo(writer);
  }

  @DisplayName("type, search 로 게시글 목록 조회한다.")
  @Test
  public void findAllByTypeAndSearch() {
    // given
    ContestBoard saveBoard = contestBoardRepository.save(CONTEST_BOARD);
    ContestBoard saveBoard2 = contestBoardRepository.save(CONTEST_BOARD_2);

    // when
    List<ContestBoardDto> dtoList =
        contestBoardRepository.findAllByTypeAndFieldAndSearch(CONTEST, null, "", DATE_CONTEST_END);

    // then
    assertThat(dtoList).hasSize(2);
    assertThat(dtoList.get(0).getTitle()).isEqualTo(saveBoard.getTitle());
  }

  @DisplayName("type, id 로 게시글 상세 조회한다.")
  @Test
  public void findByTypeAndId() {
    // given
    ContestBoard saveBoard = contestBoardRepository.save(CONTEST_BOARD);

    // when
    ContestBoard contestBoard =
        contestBoardRepository.findByTypeAndId(CONTEST, saveBoard.getId()).orElse(null);

    // then
    assertThat(contestBoard).isNotNull();
    assertThat(contestBoard.getTitle()).isEqualTo(saveBoard.getTitle());
  }
}
