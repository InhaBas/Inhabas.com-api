package com.inhabas.api.domain.project.usecase;

import static com.inhabas.api.domain.menu.domain.MenuExampleTest.getAlphaTesterMenu;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest.getProjectMenuGroup;
import static com.inhabas.api.domain.project.domain.ProjectBoardType.ALPHA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.project.domain.ProjectBoard;
import com.inhabas.api.domain.project.dto.ProjectBoardDetailDto;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;
import com.inhabas.api.domain.project.dto.SaveProjectBoardDto;
import com.inhabas.api.domain.project.repository.ProjectBoardRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ProjectBoardServiceImplTest {

  @InjectMocks ProjectBoardServiceImpl projectBoardService;

  @Mock MemberRepository memberRepository;
  @Mock ProjectBoardRepository projectBoardRepository;
  @Mock MenuRepository menuRepository;
  @Mock BoardFileRepository boardFileRepository;

  @DisplayName("project board 게시글 목록을 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getPosts() {
    // given
    ProjectBoardDto dto =
        new ProjectBoardDto(
            1L,
            "title",
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false);

    given(projectBoardRepository.findAllByTypeAndSearch(any(), any())).willReturn(List.of(dto));

    // when
    List<ProjectBoardDto> projectBoardDtoList = projectBoardService.getPosts(ALPHA, "");

    // then
    assertThat(projectBoardDtoList).hasSize(1);
  }

  @DisplayName("project board 게시글 단일 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getPost() {
    // given
    Member member = MemberTest.chiefMember();
    Menu menu = getAlphaTesterMenu(getProjectMenuGroup());
    ProjectBoard projectBoard =
        new ProjectBoard("title", menu, "content", false, LocalDateTime.now())
            .writtenBy(member, ProjectBoard.class);

    given(projectBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.of(projectBoard));

    // when
    ProjectBoardDetailDto dto = projectBoardService.getPost(1L, ALPHA, 1L);

    // then
    assertThat(dto.getTitle()).isEqualTo(projectBoard.getTitle());
  }

  @DisplayName("project board 게시글을 작성한다.")
  @Transactional
  @Test
  void write() {
    // given
    Member member = MemberTest.chiefMember();
    SaveProjectBoardDto saveProjectBoardDto = new SaveProjectBoardDto("title", "content", null, 2);
    Menu menu = getAlphaTesterMenu(getProjectMenuGroup());
    ProjectBoard projectBoard =
        new ProjectBoard("title", menu, "content", false, LocalDateTime.now())
            .writtenBy(member, ProjectBoard.class);

    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    given(projectBoardRepository.save(any())).willReturn(projectBoard);
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));

    // when
    projectBoardService.write(1L, ALPHA, saveProjectBoardDto);

    // then
    then(menuRepository).should(times(1)).findById(anyInt());
    then(projectBoardRepository).should(times(1)).save(any());
  }

  @DisplayName("project board 게시글을 수정한다.")
  @Transactional
  @Test
  void update() {
    // given
    SaveProjectBoardDto saveProjectBoardDto = new SaveProjectBoardDto("title", "content", null, 2);
    Menu menu = getAlphaTesterMenu(getProjectMenuGroup());
    ProjectBoard projectBoard =
        new ProjectBoard("title", menu, "content", false, LocalDateTime.now());
    ReflectionTestUtils.setField(projectBoard, "id", 1L);
    Member writer = MemberTest.chiefMember();

    given(memberRepository.findById(any())).willReturn(Optional.of(writer));
    given(projectBoardRepository.findById(any())).willReturn(Optional.of(projectBoard));
    given(boardFileRepository.getAllByIdInAndUploader(anyList(), any()))
        .willReturn(new ArrayList<>());

    // when
    projectBoardService.update(projectBoard.getId(), ALPHA, saveProjectBoardDto, 1L);

    // then
    then(memberRepository).should(times(1)).findById(any());
    then(projectBoardRepository).should(times(1)).findById(any());
    then(boardFileRepository).should(times(1)).getAllByIdInAndUploader(anyList(), any());
  }

  @DisplayName("project board 게시글을 삭제한다.")
  @Transactional
  @Test
  void delete() {
    // given
    Long boardId = 1L;
    doNothing().when(projectBoardRepository).deleteById(boardId);

    // when
    projectBoardService.delete(boardId);

    // then
    then(projectBoardRepository).should(times(1)).deleteById(boardId);
  }
}
