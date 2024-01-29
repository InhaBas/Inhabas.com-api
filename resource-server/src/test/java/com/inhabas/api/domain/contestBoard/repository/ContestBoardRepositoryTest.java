package com.inhabas.api.domain.contestBoard.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DefaultDataJpaTest
public class ContestBoardRepositoryTest {
  @Autowired private ContestBoardRepository contestBoardRepository;

  @Autowired TestEntityManager em;

  Menu menu;
  ContestBoard board1;
  ContestBoard board2;
  ContestBoard board3;
  Member writer;

  //    @BeforeEach
  //    public void settingContestBoard(){
  //        MenuGroup boardMenuGroup = em.persist(new MenuGroup("게시판"));
  //        writer = em.persist(basicMember1());
  //        menu = em.persist(
  //                Menu.builder()
  //                        .menuGroup(boardMenuGroup)
  //                        .priority(1)
  //                        .type(MenuType.LIST)
  //                        .name("공모전게시판")
  //                        .description("공모전 정보를 알려주는 게시판입니다.")
  //                        .build());
  //
  //        board1 = ContestBoardTest.getContestBoard1()
  //                    .writtenBy(writer.getId()).inMenu(menu.getId());
  //        board2 = ContestBoardTest.getContestBoard2()
  //                    .writtenBy(writer.getId()).inMenu(menu.getId());
  //        board3 = ContestBoardTest.getContestBoard3()
  //                    .writtenBy(writer.getId()).inMenu(menu.getId());
  //    }

  //    @DisplayName("저장한 게시글의 Id를 참조하여 Dto를 반환한다.")
  //    @Test
  //    public void findDtoByIdTest() {
  //        //given
  //        ContestBoard savedContestBoard = contestBoardRepository.save(board1);
  //
  //        DetailContestBoardDto expectedDto = DetailContestBoardDto.builder()
  //                        .id(savedContestBoard.getId())
  //                        .writerName(writer.getName())
  //                        .title(savedContestBoard.getTitle())
  //                        .content(savedContestBoard.getContent())
  //                        .association(savedContestBoard.getAssociation())
  //                        .topic(savedContestBoard.getTopic())
  //                        .start(savedContestBoard.getStart())
  //                        .deadline(savedContestBoard.getDeadline())
  //                        .build();
  //
  //        // when
  //        Optional<DetailContestBoardDto> returnedDto =
  // contestBoardRepository.findDtoById(savedContestBoard.getId());
  //
  //        // then
  //        assertThat(expectedDto)
  //                .usingRecursiveComparison()
  //                .ignoringFields("created", "updated")
  //                        .isEqualTo(returnedDto.get());
  //    }
  //
  //    @DisplayName("특정 menuId를 기준으로 모든 게시글 Dto를 조회해 Page 객체를 반환한다.")
  //    @Test
  //    public void findAllByMenuIdTest() {
  //        //given
  //        List <ListContestBoardDto> expectedDtoList = new ArrayList<>();
  //        contestBoardRepository.save(board1);
  //        contestBoardRepository.save(board2);
  //        contestBoardRepository.save(board3);
  //
  //        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "deadline");
  //
  //        expectedDtoList.add(new ListContestBoardDto(board3.getTitle(), board3.getTopic(),
  // board3.getStart(), board3.getDeadline()));
  //        expectedDtoList.add(new ListContestBoardDto(board1.getTitle(), board1.getTopic(),
  // board1.getStart(), board1.getDeadline()));
  //        expectedDtoList.add(new ListContestBoardDto(board2.getTitle(), board2.getTopic(),
  // board2.getStart(), board2.getDeadline()));
  //
  //        // when
  //        List<ListContestBoardDto> returnedDtoList =
  // contestBoardRepository.findAllByMenuId(menu.getId(), pageable).getContent();
  //
  //        // then
  //
  // assertThat(returnedDtoList).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedDtoList);
  //    }
}
