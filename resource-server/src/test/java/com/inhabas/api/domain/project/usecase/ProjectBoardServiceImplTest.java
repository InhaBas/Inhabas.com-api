package com.inhabas.api.domain.project.usecase;

import static com.inhabas.api.domain.menu.domain.MenuExampleTest.getNormalNoticeMenu;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest.getProjectMenuGroup;
import static com.inhabas.api.domain.project.ProjectBoardType.ALPHA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
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
  @Mock S3Service s3Service;

  @DisplayName("project board 게시글 목록을 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getPosts() {
    // given
    NormalBoardDto dto =
        new NormalBoardDto(
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
    List<NormalBoardDto> normalBoardDtoList = projectBoardService.getPosts(ALPHA, "");

    // then
    assertThat(normalBoardDtoList).hasSize(1);
  }

  @DisplayName("project board 게시글 단일 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getPost() {
    // given
    Member member = MemberTest.chiefMember();
    Menu menu = getNormalNoticeMenu(getProjectMenuGroup());
    NormalBoard normalBoard =
        new NormalBoard("title", menu, "content", false, LocalDateTime.now())
            .writtenBy(member, NormalBoard.class);

    given(projectBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.of(normalBoard));

    // when
    NormalBoardDetailDto dto = projectBoardService.getPost(1L, ALPHA, 1L);

    // then
    assertThat(dto.getTitle()).isEqualTo(normalBoard.getTitle());
  }

  @DisplayName("project board 게시글을 작성한다.")
  @Transactional
  @Test
  void write() {
    // given
    Member member = MemberTest.chiefMember();
    SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", "content", null, 2);
    Menu menu = getNormalNoticeMenu(getProjectMenuGroup());
    NormalBoard normalBoard =
        new NormalBoard("title", menu, "content", false, LocalDateTime.now())
            .writtenBy(member, NormalBoard.class);

    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    given(projectBoardRepository.save(any())).willReturn(normalBoard);
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));

    // when
    projectBoardService.write(1L, ALPHA, saveNormalBoardDto);

    // then
    then(menuRepository).should(times(1)).findById(anyInt());
    then(projectBoardRepository).should(times(1)).save(any());
  }

  @DisplayName("project board 게시글을 수정한다.")
  @Transactional
  @Test
  void update() {
    // given
    SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", "content", null, 2);
    Menu menu = getNormalNoticeMenu(getProjectMenuGroup());
    NormalBoard normalBoard = new NormalBoard("title", menu, "content", false, LocalDateTime.now());
    ReflectionTestUtils.setField(normalBoard, "id", 1L);

    given(projectBoardRepository.findById(any())).willReturn(Optional.of(normalBoard));
    given(projectBoardRepository.save(any())).willReturn(normalBoard);

    // when
    projectBoardService.update(normalBoard.getId(), ALPHA, saveNormalBoardDto);

    // then
    then(projectBoardRepository).should(times(1)).findById(any());
    then(projectBoardRepository).should(times(1)).save(any());
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
