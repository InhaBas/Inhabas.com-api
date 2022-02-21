package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuGroup;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.domain.menu.MenuType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(JpaConfig.class)
public class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    TestEntityManager em;

    MenuGroup menuGroup1;
    MenuGroup menuGroup2;

    @BeforeEach
    public void setUp() {
        menuGroup1 = em.persist(new MenuGroup("IBAS"));
        menuGroup2 = em.persist(new MenuGroup("게시판 목록"));
    }

    @DisplayName("새로운 메뉴를 만든다.")
    @Test
    public void CreateNewMenu() {
        //given
        Menu activityBoardMenu = new Menu(menuGroup1, 1, MenuType.LIST, "동아리 활동", "동아리원의 활동을 기록하는 게시판입니다.");
        Menu noticeBoardMenu = new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다.");
        Menu freeBoardMenu = new Menu(menuGroup2, 2, MenuType.LIST, "자유게시판", "부원이 자유롭게 글을 작성할 수 있는 게시판입니다.");

        //when
        Menu saveActivityMenu = menuRepository.save(activityBoardMenu);
        Menu saveNoticeMenu = menuRepository.save(noticeBoardMenu);
        Menu saveFreeMenu = menuRepository.save(freeBoardMenu);

        //then
        assertThat(saveActivityMenu)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(activityBoardMenu);
    }

    @DisplayName("메뉴 이름을 수정한다.")
    @Test
    public void UpdateMenuName() {
        //given
        Menu noticeMenu = menuRepository.save(new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다."));
        em.flush();em.clear();

        //when
        String newName = "공지 사항";
        Menu updated = menuRepository.save(
                new Menu(noticeMenu.getId(), noticeMenu.getMenuGroup(), noticeMenu.getPriority(), noticeMenu.getType(), newName, noticeMenu.getDescription()));

        //then
        assertThat(updated.getName()).isEqualTo(newName);
    }

    @DisplayName("한 메뉴그룹에, priority 가 중복될 시 오류")
    @Test
    public void CannotSamePriorityValue() {
        /*
        이 테스트는 데이터베이스의 unique key 제약 조건을 검사함.
        하지만 현재 테스트는 h2 인메모리 방식으로 테이블을 생성해서 진행 중.
        실제 db 상태와는 무관함. 추후 테스트 방식을 고려해봐야함.
         */

        //given
        Menu activityBoardMenu = new Menu(menuGroup1, 1, MenuType.LIST, "동아리 활동", "동아리원의 활동을 기록하는 게시판입니다.");
        Menu noticeBoardMenu = new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다.");
        Menu freeBoardMenu = new Menu(menuGroup2, 2, MenuType.LIST, "자유게시판", "부원이 자유롭게 글을 작성할 수 있는 게시판입니다.");
        menuRepository.save(activityBoardMenu);
        menuRepository.save(noticeBoardMenu);
        menuRepository.save(freeBoardMenu);

        //when
        assertThrows(DataIntegrityViolationException.class,
                () -> menuRepository.save(new Menu(menuGroup2, 2, MenuType.LIST, "질문게시판", "궁금한 점을 질문하는 게시판입니다.")));
    }


    @DisplayName("메뉴 ID로 메뉴 타입 조회")
    @Test
    public void findMenuTypeTest() {
        // given
        Menu noticeBoardMenu = new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다.");
        menuRepository.save(noticeBoardMenu);

        // when
        Optional<MenuType> menuType = menuRepository.findMenuTypeByMenuId(noticeBoardMenu.getId());

        // then
        assertThat(menuType.get()).isEqualTo(MenuType.LIST);
    }

}