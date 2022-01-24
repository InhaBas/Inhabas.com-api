package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuGroup;
import com.inhabas.api.domain.menu.MenuType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
public class BaseEntityTest {

    @PersistenceContext
    EntityManager em;

    Menu freeBoardMenu;

    @BeforeEach
    public void setUp() {
        MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
        freeBoardMenu = em.persist(
                Menu.builder()
                .menuGroup(boardMenuGroup)
                .priority(2)
                .type(MenuType.LIST)
                .name("자유게시판")
                .description("부원이 자유롭게 사용할 수 있는 게시판입니다.")
                .build());
    }

    @Test
    public void createdTimeTest() {
        //given
        em.persist(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(MEMBER1)
                .inMenu(freeBoardMenu);

        //when
        em.persist(board);

        //then
        assertThat(board.getCreated()).isNotNull();
        assertThat(board.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void updatedTimeTest() {
        //given
        em.persist(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(MEMBER1)
                .inMenu(freeBoardMenu);
        em.persist(board);

        //when
        NormalBoard param = new NormalBoard(board.getId(), "new title", "new contents")
                .writtenBy(MEMBER1)
                .inMenu(freeBoardMenu);
        em.merge(param);
        em.flush();em.clear();

        //then
        NormalBoard find = em.find(NormalBoard.class, board.getId());
        assertThat(find.getUpdated()).isNotNull();
        assertThat(find.getUpdated()).isInstanceOf(LocalDateTime.class);
    }

}
