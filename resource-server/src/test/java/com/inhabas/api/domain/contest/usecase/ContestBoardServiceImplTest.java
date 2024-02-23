package com.inhabas.api.domain.contest.usecase;

import static com.inhabas.api.domain.contest.domain.valueObject.ContestType.ACTIVITY;
import static com.inhabas.api.domain.menu.domain.MenuExampleTest.getContestMenu;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest.getContestMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.ContestField;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.contest.repository.ContestFieldRepository;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ContestBoardServiceImplTest {

  @InjectMocks private ContestBoardServiceImpl contestBoardService;

  @Mock private ContestBoardRepository contestBoardRepository;

  @Mock private ContestFieldRepository contestFieldRepository;

  @Mock private MemberRepository memberRepository;

  @Mock private MenuRepository menuRepository;

  @Mock private S3Service s3Service;

  @DisplayName("Contest board 게시글 목록을 조회한다.")
  @Test
  void getContestBoards() {
    // given
    ContestBoardDto contestBoardDto =
        ContestBoardDto.builder()
            .id(1L)
            .contestFieldId(1L)
            .title("테스트 제목")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .thumbnail(new FileDownloadDto("thumbnail.jpg", "thumbnailUrl"))
            .build();

    given(
            contestBoardRepository.findAllByTypeAndFieldAndSearch(
                any(ContestType.class), anyLong(), anyString(), anyString()))
        .willReturn(List.of(contestBoardDto));

    // when
    List<ContestBoardDto> result = contestBoardService.getContestBoards(ACTIVITY, 1L, "", "title");

    // then
    Assertions.assertThat(result).hasSize(1);
  }

  @DisplayName("Contest board 게시글 단일 조회한다.")
  @Test
  void getContestBoard() {
    // given
    Member member = MemberTest.chiefMember();
    Menu menu = getContestMenu(getContestMenuGroup());
    ContestField contestField = ContestField.builder().name("빅데이터").build();

    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목")
            .content("테스트 내용")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build()
            .writtenBy(member, ContestBoard.class);

    given(contestBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.of(contestBoard));

    // when
    ContestBoardDetailDto dto = contestBoardService.getContestBoard(ACTIVITY, 1L);

    // then
    assertThat(dto).isNotNull();
    assertThat(dto.getTitle()).isEqualTo("테스트 제목");
    assertThat(dto.getContent()).isEqualTo("테스트 내용");
    assertThat(dto.getAssociation()).isEqualTo("(주) 아이바스");
    assertThat(dto.getTopic()).isEqualTo("테스트 주제");
    assertThat(dto.getDateContestStart()).isEqualTo(LocalDate.now());
    assertThat(dto.getDateContestEnd()).isEqualTo(LocalDate.now().plusDays(10));
  }

  @DisplayName("Contest board 게시글을 작성한다.")
  @Test
  void writeContestBoard() {
    // given
    Member member = MemberTest.chiefMember();

    ContestField contestField = ContestField.builder().name("빅데이터").build();
    ReflectionTestUtils.setField(contestField, "id", 1L);
    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(contestField.getId())
            .title("테스트 제목")
            .content("테스트 내용")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .files(null)
            .build();

    Menu menu = getContestMenu(getContestMenuGroup());
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목")
            .content("테스트 내용")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build()
            .writtenBy(member, ContestBoard.class);

    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    given(contestBoardRepository.save(any())).willReturn(contestBoard);
    given(contestFieldRepository.findById(contestField.getId()))
        .willReturn(Optional.of(contestField));
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));

    // when
    contestBoardService.writeContestBoard(1L, ACTIVITY, saveContestBoardDto);
    // then
    then(menuRepository).should(times(1)).findById(anyInt());
    then(contestBoardRepository).should(times(1)).save(any());
  }

  @DisplayName("Contest board 게시글을 수정한다.")
  @Test
  void updateContestBoard() {
    // given
    SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto();
    Menu menu = getContestMenu(getContestMenuGroup());
    ContestField contestField = ContestField.builder().name("빅데이터").build();
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목")
            .content("테스트 내용")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();
    ReflectionTestUtils.setField(contestBoard, "id", 1L);

    given(contestBoardRepository.findById(any())).willReturn(Optional.of(contestBoard));
    given(contestBoardRepository.save(any())).willReturn(contestBoard);

    // when
    contestBoardService.updateContestBoard(contestBoard.getId(), ACTIVITY, saveContestBoardDto);

    // then
    then(contestBoardRepository).should(times(1)).findById(any());
    then(contestBoardRepository).should(times(1)).save(any());
  }

  @DisplayName("Contest board 게시글을 삭제한다.")
  @Test
  void deleteContestBoard() {
    // given
    Long boardId = 1L;
    doNothing().when(contestBoardRepository).deleteById(boardId);
    // when
    contestBoardService.deleteContestBoard(boardId);

    // then
    then(contestBoardRepository).should(times(1)).deleteById(boardId);
  }
}
