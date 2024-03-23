package com.inhabas.api.domain.scholarship.usecase;

import static com.inhabas.api.domain.menu.domain.MenuExampleTest.getScholarshipMenu;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest.getScholarshipMenuGroup;
import static com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType.SPONSOR;
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

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.scholarship.domain.Scholarship;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDetailDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipBoardRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ScholarshipBoardServiceTest {

  @InjectMocks ScholarshipBoardServiceImpl scholarshipBoardService;

  @Mock MemberRepository memberRepository;
  @Mock ScholarshipBoardRepository scholarshipBoardRepository;
  @Mock MenuRepository menuRepository;
  @Mock BoardFileRepository boardFileRepository;

  private static final Long MEMBER_ID = 1L;
  private static final Long SCHOLARSHIP_ID = 1L;

  private Member writer;
  private Menu menu;
  private Scholarship scholarship;

  @BeforeEach
  void setUp() {
    writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", MEMBER_ID);
    menu = getScholarshipMenu(getScholarshipMenuGroup());
    scholarship =
        new Scholarship("title", menu, "content", LocalDateTime.now())
            .writtenBy(writer, Scholarship.class);
    ReflectionTestUtils.setField(scholarship, "id", SCHOLARSHIP_ID);
  }

  @DisplayName("scholarship board 게시글 목록을 조회한다.")
  @Test
  void getPosts() {
    // given
    ScholarshipBoardDto dto =
        new ScholarshipBoardDto(1L, "title", writer, LocalDateTime.now(), LocalDateTime.now());

    given(scholarshipBoardRepository.findAllByTypeAndSearch(any(), any())).willReturn(List.of(dto));

    // when
    List<ScholarshipBoardDto> scholarshipBoardDtoList =
        scholarshipBoardService.getPosts(SPONSOR, "");

    // then
    assertThat(scholarshipBoardDtoList).hasSize(1);
  }

  @DisplayName("scholarship board 게시글 단일 조회한다.")
  @Test
  void getPost() {
    // given
    given(scholarshipBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.of(scholarship));

    // when
    ScholarshipBoardDetailDto dto = scholarshipBoardService.getPost(SPONSOR, 1L, 1L);

    // then
    assertThat(dto.getTitle()).isEqualTo("title");
  }

  @DisplayName("scholarship board 게시글을 작성한다.")
  @Test
  void write() {
    // given
    SaveScholarshipBoardDto saveScholarshipBoardDto =
        new SaveScholarshipBoardDto("title", "content", LocalDateTime.now(), null);

    given(memberRepository.findById(any())).willReturn(Optional.of(writer));
    given(scholarshipBoardRepository.save(any())).willReturn(scholarship);
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));
    given(boardFileRepository.getAllByIdInAndUploader(anyList(), any()))
        .willReturn(new ArrayList<>());

    // when
    scholarshipBoardService.write(SPONSOR, saveScholarshipBoardDto, 1L);

    // then
    then(menuRepository).should(times(1)).findById(anyInt());
    then(scholarshipBoardRepository).should(times(1)).save(any());
    then(menuRepository).should(times(1)).findById(anyInt());
    then(boardFileRepository).should(times(1)).getAllByIdInAndUploader(anyList(), any());
  }

  @DisplayName("scholarship board 게시글을 수정한다.")
  @Test
  void update() {
    // given
    SaveScholarshipBoardDto saveScholarshipBoardDto =
        new SaveScholarshipBoardDto("title", "content", LocalDateTime.now(), null);

    given(memberRepository.findById(any())).willReturn(Optional.of(writer));
    given(scholarshipBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.of(scholarship));
    given(boardFileRepository.getAllByIdInAndUploader(anyList(), any()))
        .willReturn(new ArrayList<>());

    // when
    scholarshipBoardService.update(1L, SPONSOR, saveScholarshipBoardDto, 1L);

    // then
    then(memberRepository).should(times(1)).findById(any());
    then(scholarshipBoardRepository).should(times(1)).findByTypeAndId(any(), any());
    then(boardFileRepository).should(times(1)).getAllByIdInAndUploader(anyList(), any());
  }

  @DisplayName("scholarship board 게시글을 삭제한다.")
  @Test
  void delete() {
    // given
    doNothing().when(scholarshipBoardRepository).delete(any());
    given(scholarshipBoardRepository.findByTypeAndId(any(), any()))
        .willReturn(Optional.ofNullable(scholarship));

    // when
    scholarshipBoardService.delete(SPONSOR, 1L);

    // then
    then(scholarshipBoardRepository).should(times(1)).findByTypeAndId(any(), any());
    then(scholarshipBoardRepository).should(times(1)).delete(any());
  }
}
