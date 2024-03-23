package com.inhabas.api.domain.contest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContestBoardTest {

  @Mock private Menu menu;
  @Mock private Member writer;
  @Mock private ContestField contestField;

  private ContestBoard contestBoard;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Long contestFieldId = 1L;
    String title = "테스트 공모전";
    String content = "테스트 내용";
    String association = "테스트 협회";
    String topic = "테스트 주제";
    LocalDate dateContestStart = LocalDate.of(2023, 1, 1);
    LocalDate dateContestEnd = LocalDate.of(2023, 12, 31);

    when(contestField.getId()).thenReturn(contestFieldId);

    contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title(title)
            .content(content)
            .association(association)
            .topic(topic)
            .dateContestStart(dateContestStart)
            .dateContestEnd(dateContestEnd)
            .build();
  }

  @Test
  @DisplayName("올바른 ContestBoard를 생성한다.")
  void createContestBoardTest() {
    // Then
    assertThat(contestBoard.getContestField().getId()).isEqualTo(1L);
    assertThat(contestBoard.getTitle()).isEqualTo("테스트 공모전");
    assertThat(contestBoard.getContent()).isEqualTo("테스트 내용");
    assertThat(contestBoard.getAssociation()).isEqualTo("테스트 협회");
    assertThat(contestBoard.getTopic()).isEqualTo("테스트 주제");
    assertThat(contestBoard.getDateContestStart()).isEqualTo(LocalDate.of(2023, 1, 1));
    assertThat(contestBoard.getDateContestEnd()).isEqualTo(LocalDate.of(2023, 12, 31));
  }

  @Test
  @DisplayName("ContestBoard 정보를 수정한다.")
  void updateContestBoardTest() {
    // Given

    ContestBoard contestBoard = new ContestBoard();
    Long newContestFieldId = 2L;
    String newTitle = "새로운 제목";
    String newContent = "새로운 내용";
    String newAssociation = "새로운 협회";
    String newTopic = "새로운 주제";
    LocalDate newDateContestStart = LocalDate.now();
    LocalDate newDateContestEnd = LocalDate.now().plusDays(30);

    ContestField newContestField = new ContestField("새로운 분야");
    ReflectionTestUtils.setField(newContestField, "id", newContestFieldId);

    // When
    contestBoard.updateContest(
        newContestField,
        newTitle,
        newContent,
        newAssociation,
        newTopic,
        newDateContestStart,
        newDateContestEnd);

    // Then
    assertThat(contestBoard.getContestField().getId()).isEqualTo(newContestFieldId);
    assertThat(contestBoard.getTitle()).isEqualTo(newTitle);
    assertThat(contestBoard.getContent()).isEqualTo(newContent);
    assertThat(contestBoard.getAssociation()).isEqualTo(newAssociation);
    assertThat(contestBoard.getTopic()).isEqualTo(newTopic);
    assertThat(contestBoard.getDateContestStart()).isEqualTo(newDateContestStart);
    assertThat(contestBoard.getDateContestEnd()).isEqualTo(newDateContestEnd);
  }

  @Test
  @DisplayName("ContestBoard의 첨부 파일을 수정한다.")
  void updateFilesTest() {
    // Given

    List<BoardFile> files = new ArrayList<>();
    files.add(new BoardFile("random", "file1.jpg", "/url1", writer, 10L, "image/jpeg"));
    files.add(new BoardFile("random", "file2.pdf", "/url2", writer, 10L, "image/jpeg"));

    // When
    contestBoard.updateFiles(files);

    // Then
    assertThat(contestBoard.getFiles()).hasSize(2);
    assertThat(contestBoard.getFiles().get(0).getName()).isEqualTo("file1.jpg");
    assertThat(contestBoard.getFiles().get(0).getUrl()).isEqualTo("/url1");
    assertThat(contestBoard.getFiles().get(1).getName()).isEqualTo("file2.pdf");
    assertThat(contestBoard.getFiles().get(1).getUrl()).isEqualTo("/url2");
  }
}
