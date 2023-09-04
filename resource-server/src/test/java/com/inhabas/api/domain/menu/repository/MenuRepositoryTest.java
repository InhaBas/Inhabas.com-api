package com.inhabas.api.domain.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.menu.dto.MenuDto;
import com.inhabas.api.domain.menu.dto.MenuGroupDto;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

@DefaultDataJpaTest
public class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    TestEntityManager em;


    @BeforeEach
    public void setUp() {

    }

    @DisplayName("새로운 메뉴를 만든다.")
    @Test
    public void CreateNewMenu() {
        //given
        MenuGroup menuGroup1 = em.persist(new MenuGroup("IBAS"));
        Menu activityBoardMenu = new Menu(menuGroup1, 1, MenuType.LIST, "동아리 활동", "동아리원의 활동을 기록하는 게시판입니다.");

        //when
        Menu saveActivityMenu = menuRepository.save(activityBoardMenu);
        em.flush();

        //then
        assertThat(saveActivityMenu.getId()).isNotNull();
        assertThat(saveActivityMenu.getDateCreated()).isNotNull();
        assertThat(saveActivityMenu.getDateUpdated()).isNotNull();
        assertThat(saveActivityMenu)
                .usingRecursiveComparison()
                .ignoringFields("id", "created", "updated")
                .isEqualTo(activityBoardMenu);
    }

    @Disabled
    @DisplayName("메뉴 이름을 수정한다.")
    @Test
    public void UpdateMenuName() {
        //given
        MenuGroup menuGroup1 = em.persist(new MenuGroup("IBAS"));
        MenuGroup menuGroup2 = em.persist(new MenuGroup("게시판 목록"));
        Menu noticeMenu = menuRepository.save(new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다."));
        em.flush();em.clear();

        //when
        String newName = "공지 사항";
        Menu param = new Menu(noticeMenu.getMenuGroup(), noticeMenu.getPriority(), noticeMenu.getType(), newName, noticeMenu.getDescription());
        Integer menuId = (Integer) ReflectionTestUtils.getField(param, "id");
        ReflectionTestUtils.setField(param, "id", menuId);
        //Menu updated = menuRepository.save(param);  // service 로 제대로 구현한 뒤에 테스트 해야함.

        //then
        //assertThat(updated.getName()).isEqualTo(newName);
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
        MenuGroup menuGroup1 = em.persist(new MenuGroup("IBAS"));
        MenuGroup menuGroup2 = em.persist(new MenuGroup("게시판 목록"));

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

    @DisplayName("MenuGroup 별 메뉴를 모두 반환한다.")
    @Test
    public void findAllMenuGroupByMenuGroup() {
        //given
        MenuGroup menuGroup1 = em.persist(new MenuGroup("IBAS"));
        MenuGroup menuGroup2 = em.persist(new MenuGroup("게시판 목록"));
        MenuGroup menuGroup3 = em.persist(new MenuGroup("강의실"));
        MenuGroup menuGroup4 = em.persist(new MenuGroup("프로젝트"));
        MenuGroup menuGroup5 = em.persist(new MenuGroup("취미모임"));

        Menu menu11 = new Menu(menuGroup1, 1, MenuType.LIST, "동아리 활동", "동아리원의 활동을 기록하는 게시판입니다.");
        Menu menu12 = new Menu(menuGroup1, 2, MenuType.LIST, "동아리 소개", "동아리를 소개하는 게시판입니다.");
        Menu menu13 = new Menu(menuGroup1, 3, MenuType.LIST, "회계장부", "동아리 회계 내역을 공개합니다.");
        Menu menu14 = new Menu(menuGroup1, 4, MenuType.LIST, "예산지원신청", "내부 활동에 필요한 예산을 신청할 수 있습니다.");
        Menu menu21 = new Menu(menuGroup2, 1, MenuType.LIST, "공지사항", "동아리 공지를 게시하는 게시판입니다.");
        Menu menu22 = new Menu(menuGroup2, 2, MenuType.LIST, "자유게시판", "부원이 자유롭게 작성하는 게시판입니다.");
        Menu menu23 = new Menu(menuGroup2, 3, MenuType.LIST, "건의사항", "필요하다고 생각되는 사항들을 요청하는 게시판입니다.");
        Menu menu24 = new Menu(menuGroup2, 4, MenuType.LIST, "자료실", "자료실입니다.");
        Menu menu31 = new Menu(menuGroup3, 1, MenuType.LIST, "강의", "부원이 서로 가르치고 배우는 곳입니다.");
        Menu menu32 = new Menu(menuGroup3, 2, MenuType.LIST, "스터디", "같이 공부하는 곳입니다..");
        Menu menu41 = new Menu(menuGroup4, 1, MenuType.LIST, "알파프로젝트", "동아리 내부 알파프로젝트 게시판입니다.");
        Menu menu42 = new Menu(menuGroup4, 2, MenuType.LIST, "베타프로젝트", "동아리 내부 베타프로젝트 게시판입니다.");
        Menu menu51 = new Menu(menuGroup5, 1, MenuType.LIST, "취미모임", "취미를 공유하며 친목을 다져보아요.");
        List<Menu> menuList = Arrays.asList(menu11, menu12, menu13, menu14, menu21, menu22, menu23, menu24, menu31, menu32, menu41, menu42, menu51);

        menuList = menuRepository.saveAllAndFlush(menuList);
        em.clear();

        //when
        List<MenuGroupDto> allMenuInfo = menuRepository.findAllMenuByMenuGroup();

        //then
        MenuGroupDto menuGroupDto1 = new MenuGroupDto(
                menuGroup1.getId(),
                menuGroup1.getName(),
                Arrays.asList(MenuDto.convert(menuList.get(0)), MenuDto.convert(menuList.get(1)), MenuDto.convert(menuList.get(2)), MenuDto.convert(menuList.get(3))));
        MenuGroupDto menuGroupDto2 = new MenuGroupDto(
                menuGroup2.getId(),
                menuGroup2.getName(),
                Arrays.asList(MenuDto.convert(menuList.get(4)), MenuDto.convert(menuList.get(5)), MenuDto.convert(menuList.get(6)), MenuDto.convert(menuList.get(7))));
        MenuGroupDto menuGroupDto3 = new MenuGroupDto(
                menuGroup3.getId(),
                menuGroup3.getName(),
                Arrays.asList(MenuDto.convert(menuList.get(8)), MenuDto.convert(menuList.get(9))));
        MenuGroupDto menuGroupDto4 = new MenuGroupDto(
                menuGroup4.getId(),
                menuGroup4.getName(),
                Arrays.asList(MenuDto.convert(menuList.get(10)), MenuDto.convert(menuList.get(11))));
        MenuGroupDto menuGroupDto5 = new MenuGroupDto(
                menuGroup5.getId(),
                menuGroup5.getName(),
                List.of(MenuDto.convert(menuList.get(12))));
        List<MenuGroupDto> expectedMenuGroupDtoList = Arrays.asList(menuGroupDto1, menuGroupDto2, menuGroupDto3, menuGroupDto4, menuGroupDto5);

        assertThat(allMenuInfo)
                .usingRecursiveComparison()
                .isEqualTo(expectedMenuGroupDtoList);
    }

}
