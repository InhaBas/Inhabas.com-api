package com.inhabas.api.domain;


import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.contest.ContestBoard;
import com.inhabas.api.domain.contest.ContestBoardRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuGroup;
import com.inhabas.api.domain.menu.MenuType;
import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaConfig.class)
public class ContestBoardRepositoryTest {
    @Autowired
    private ContestBoardRepository contestBoardRepository;

    @Autowired
    TestEntityManager em;

    Menu menu;
    ContestBoard board1;
    ContestBoard board2;
    ContestBoard board3;

    @BeforeEach
    public void settingContestBoard(){
        MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
        Member savedMember = em.persist(MEMBER1);
        menu = em.persist(
                Menu.builder()
                        .menuGroup(boardMenuGroup)
                        .priority(1)
                        .type(MenuType.LIST)
                        .name("공모전게시판")
                        .description("공모전 정보를 알려주는 게시판입니다.")
                        .build());

        board1 = ContestBoardTest.getContestBoard1()
                    .writtenBy(savedMember).inMenu(menu);
        board2 = ContestBoardTest.getContestBoard2()
                    .writtenBy(savedMember).inMenu(menu);
        board3 = ContestBoardTest.getContestBoard3()
                    .writtenBy(savedMember).inMenu(menu);
    }

    @DisplayName("저장한 게시글의 Id를 참조하여 Dto를 반환한다.")
    @Test
    public void findDtoByIdTest() {
        //given
        ContestBoard savedContestBoard = contestBoardRepository.save(board1);

        DetailContestBoardDto expectedDto = DetailContestBoardDto.builder()
                        .id(savedContestBoard.getId())
                        .writerName(savedContestBoard.getWriter().getName())
                        .title(savedContestBoard.getTitle())
                        .contents(savedContestBoard.getContents())
                        .association(savedContestBoard.getAssociation())
                        .topic(savedContestBoard.getTopic())
                        .start(savedContestBoard.getStart())
                        .deadline(savedContestBoard.getDeadline())
                        .build();

        // when
        Optional<DetailContestBoardDto> returnedDto = contestBoardRepository.findDtoById(savedContestBoard.getId());

        // then
        assertThat(expectedDto)
                .usingRecursiveComparison()
                .ignoringFields("created", "updated")
                        .isEqualTo(returnedDto.get());
    }

    @DisplayName("특정 menuId를 기준으로 모든 게시글 Dto를 조회해 Page 객체를 반환한다.")
    @Test
    public void findAllByMenuIdTest() {
        //given
        List <ListContestBoardDto> expectedDtoList = new ArrayList<>();
        contestBoardRepository.save(board1);
        contestBoardRepository.save(board2);
        contestBoardRepository.save(board3);

        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "deadline");

        expectedDtoList.add(new ListContestBoardDto(board3.getTitle(), board3.getTopic(), board3.getStart(), board3.getDeadline()));
        expectedDtoList.add(new ListContestBoardDto(board1.getTitle(), board1.getTopic(), board1.getStart(), board1.getDeadline()));
        expectedDtoList.add(new ListContestBoardDto(board2.getTitle(), board2.getTopic(), board2.getStart(), board2.getDeadline()));

        // when
        List<ListContestBoardDto> returnedDtoList = contestBoardRepository.findAllByMenuId(menu.getId(), pageable).getContent();

        // then
        assertThat(returnedDtoList).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedDtoList);
    }
}
