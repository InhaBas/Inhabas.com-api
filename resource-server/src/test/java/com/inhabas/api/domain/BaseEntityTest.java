package com.inhabas.api.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.inhabas.api.JpaConfig;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;

@DefaultDataJpaTest
@Import(JpaConfig.class)
public class BaseEntityTest {

  @Autowired TestEntityManager em;

  Menu freeBoardMenu;

  @BeforeEach
  public void setUp() {
    MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
    freeBoardMenu =
        em.persist(
            Menu.builder()
                .menuGroup(boardMenuGroup)
                .priority(2)
                .type(MenuType.LIST)
                .name("자유게시판")
                .description("부원이 자유롭게 사용할 수 있는 게시판입니다.")
                .build());
  }

  //    @Test
  //    public void createdTimeTest() {
  //        //given
  //        Member member = em.persist(basicMember1());
  //        NormalBoard board = new NormalBoard("title", "content")
  //                .writtenBy(member.getId())
  //                .inMenu(freeBoardMenu.getId());
  //
  //        //when
  //        em.persist(board);
  //
  //        //then
  //        assertThat(board.getDateCreated()).isNotNull();
  //        assertThat(board.getDateCreated()).isInstanceOf(LocalDateTime.class);
  //    }

  //    @Test
  //    public void updatedTimeTest() {
  //        //given
  //        Member member = em.persist(basicMember1());
  //        NormalBoard board = new NormalBoard("title", "content")
  //                .writtenBy(member.getId())
  //                .inMenu(freeBoardMenu.getId());
  //        em.persist(board);
  //
  //        //when
  //        board.modify("title2", "modified content", member.getId());
  //        em.merge(board);
  //        em.flush();em.clear();
  //
  //        //then
  //        NormalBoard find = em.find(NormalBoard.class, board.getId());
  //        assertThat(find.getDateUpdated()).isNotNull();
  //        assertThat(find.getDateUpdated()).isInstanceOf(LocalDateTime.class);
  //    }

}
