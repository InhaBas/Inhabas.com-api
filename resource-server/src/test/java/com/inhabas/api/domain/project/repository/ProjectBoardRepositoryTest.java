package com.inhabas.api.domain.project.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.project.ProjectBoardType.ALPHA;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

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

@DefaultDataJpaTest
public class ProjectBoardRepositoryTest {

  @Autowired ProjectBoardRepository projectBoardRepository;
  @Autowired TestEntityManager em;

  NormalBoard PROJECT_BOARD;
  NormalBoard PROJECT_BOARD_2;
  Member writer;

  @BeforeEach
  public void setUp() {
    em.getEntityManager()
        .createNativeQuery("ALTER TABLE MENU ALTER COLUMN `id` RESTART WITH 16")
        .executeUpdate();
    writer = em.persist(basicMember1());
    MenuGroup boardMenuGroup = em.persist(new MenuGroup("프로젝트"));
    Menu projectBoardMenu =
        em.persist(
            Menu.builder()
                .menuGroup(boardMenuGroup)
                .priority(1)
                .type(MenuType.TESTER)
                .name("알파테스터")
                .description("IBAS 내부 컨테스트, 알파테스터 관련 게시판입니다.")
                .build());
    ReflectionTestUtils.setField(projectBoardMenu, "id", 16);
    PROJECT_BOARD =
        NormalBoardExampleTest.getBoard1(projectBoardMenu).writtenBy(writer, NormalBoard.class);
    PROJECT_BOARD_2 =
        NormalBoardExampleTest.getBoard2(projectBoardMenu).writtenBy(writer, NormalBoard.class);
  }

  @AfterEach
  public void deleteAll() {

    this.projectBoardRepository.deleteAll();
    this.em.clear();
  }

  @DisplayName("저장 후 반환값이 처음과 같다.")
  @Test
  public void save() {
    // when
    NormalBoard saveBoard = projectBoardRepository.save(PROJECT_BOARD);

    // then
    assertThat(saveBoard.getWriter()).isEqualTo(writer);
  }

  @DisplayName("memberId, type, search 로 게시글 목록 조회한다.")
  @Test
  public void findAllByMemberIdAndTypeAndSearch() {
    // given
    NormalBoard saveBoard = projectBoardRepository.save(PROJECT_BOARD);
    NormalBoard saveBoard2 = projectBoardRepository.save(PROJECT_BOARD_2);
    Long writerId = writer.getId();

    // when
    List<NormalBoardDto> dtoList =
        projectBoardRepository.findAllByMemberIdAndTypeAndSearch(writerId, ALPHA, "");

    // then
    assertThat(dtoList).hasSize(2);
    assertThat(dtoList.get(0).getTitle()).isEqualTo(saveBoard.getTitle());
  }

  @DisplayName("type, search 로 게시글 목록 조회한다.")
  @Test
  public void findAllByTypeAndSearch() {
    // given
    NormalBoard saveBoard = projectBoardRepository.save(PROJECT_BOARD);
    NormalBoard saveBoard2 = projectBoardRepository.save(PROJECT_BOARD_2);

    // when
    List<NormalBoardDto> dtoList = projectBoardRepository.findAllByTypeAndSearch(ALPHA, "");

    // then
    assertThat(dtoList).hasSize(2);
    assertThat(dtoList.get(0).getTitle()).isEqualTo(saveBoard.getTitle());
  }

  @DisplayName("memberId, type, id 로 게시글 상세 조회한다.")
  @Test
  public void findByMemberIdAndTypeAndId() {
    // given
    NormalBoard saveBoard = projectBoardRepository.save(PROJECT_BOARD);
    Long writerId = writer.getId();

    // when
    NormalBoard normalBoard =
        projectBoardRepository
            .findByMemberIdAndTypeAndId(writerId, ALPHA, saveBoard.getId())
            .orElse(null);

    // then
    assertThat(normalBoard).isNotNull();
    assertThat(normalBoard.getTitle()).isEqualTo(saveBoard.getTitle());
  }

  @DisplayName("type, id 로 게시글 상세 조회한다.")
  @Test
  public void findByTypeAndId() {
    // given
    NormalBoard saveBoard = projectBoardRepository.save(PROJECT_BOARD);

    // when
    NormalBoard normalBoard =
        projectBoardRepository.findByTypeAndId(ALPHA, saveBoard.getId()).orElse(null);

    // then
    assertThat(normalBoard).isNotNull();
    assertThat(normalBoard.getTitle()).isEqualTo(saveBoard.getTitle());
  }
}
