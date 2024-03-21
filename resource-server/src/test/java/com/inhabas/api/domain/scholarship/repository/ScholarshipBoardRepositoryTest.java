package com.inhabas.api.domain.scholarship.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType.SPONSOR;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.scholarship.domain.Scholarship;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardExampleTest;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class ScholarshipBoardRepositoryTest {

  @Autowired ScholarshipBoardRepository scholarshipBoardRepository;
  @Autowired TestEntityManager em;

  Scholarship scholarshipBoard1;
  Scholarship scholarshipBoard2;
  Member writer;

  @BeforeEach
  public void setUp() {
    em.getEntityManager()
        .createNativeQuery("ALTER TABLE MENU ALTER COLUMN `id` RESTART WITH 20")
        .executeUpdate();
    writer = em.persist(basicMember1());
    MenuGroup boardMenuGroup = em.persist(new MenuGroup("장학회"));
    Menu scholarshipBoardMenu =
        em.persist(
            Menu.builder()
                .menuGroup(boardMenuGroup)
                .priority(1)
                .type(MenuType.SPONSOR)
                .name("후원 내역")
                .description("후원 내역")
                .build());
    ReflectionTestUtils.setField(scholarshipBoardMenu, "id", 20);
    scholarshipBoard1 =
        ScholarshipBoardExampleTest.getBoard1(scholarshipBoardMenu)
            .writtenBy(writer, Scholarship.class);
    scholarshipBoard2 =
        ScholarshipBoardExampleTest.getBoard2(scholarshipBoardMenu)
            .writtenBy(writer, Scholarship.class);

    em.persist(scholarshipBoard1);
    em.persist(scholarshipBoard2);
  }

  @DisplayName("type, search 로 게시글 목록 조회한다.")
  @Test
  public void findAllByTypeAndSearch() {
    // when
    List<ScholarshipBoardDto> dtoList =
        scholarshipBoardRepository.findAllByTypeAndSearch(SPONSOR, "");

    // then
    assertThat(dtoList).hasSize(2);
    assertThat(dtoList.get(0).getTitle()).isEqualTo("제목1");
  }

  @DisplayName("type, id 로 게시글 상세 조회한다.")
  @Test
  public void findByTypeAndId() {
    // given
    Long savedId = scholarshipBoard1.getId();

    // when
    Optional<Scholarship> result = scholarshipBoardRepository.findByTypeAndId(SPONSOR, savedId);

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getTitle()).isEqualTo("제목1");
  }
}
