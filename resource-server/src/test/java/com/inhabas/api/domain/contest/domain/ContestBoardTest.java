package com.inhabas.api.domain.contest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.file.domain.BoardFile;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContestBoardTest {

  @Mock private ContestField contestField;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(contestField.getId()).thenReturn(1L);
  }

  @Test
  @DisplayName("올바른 ContestBoard를 생성한다.")
  void createContestBoardTest() {
    // Given
    LocalDate dateContestStart = LocalDate.of(2023, 1, 1);
    LocalDate dateContestEnd = LocalDate.of(2023, 12, 31);
    var title = "테스트 공모전";
    var content = "테스트 내용";
    var association = "테스트 협회";
    var topic = "테스트 주제";

    // When
    ContestBoard contestBoard =
        ContestBoard.builder()
            .contestType(ContestType.CONTEST)
            .contestFieldId(contestField.getId())
            .title(title)
            .content(content)
            .association(association)
            .topic(topic)
            .dateContestStart(dateContestStart)
            .dateContestEnd(dateContestEnd)
            .build();

    // Then
    assertThat(contestBoard.getContestType()).isEqualTo(ContestType.CONTEST);
    assertThat(contestBoard.getContestField().getId()).isEqualTo(contestField.getId());
    assertThat(contestBoard.getTitle()).isEqualTo(title);
    assertThat(contestBoard.getContent()).isEqualTo(content);
    assertThat(contestBoard.getAssociation()).isEqualTo(association);
    assertThat(contestBoard.getTopic()).isEqualTo(topic);
    assertThat(contestBoard.getDateContestStart()).isEqualTo(dateContestStart);
    assertThat(contestBoard.getDateContestEnd()).isEqualTo(dateContestEnd);
  }

  @Test
  @DisplayName("ContestBoard에 첨부 파일을 추가한다.")
  void addFilesToContestBoardTest() {
    // Given
    ContestBoard contestBoard = new ContestBoard();
    List<BoardFile> files = new ArrayList<>();
    files.add(new BoardFile("file1.jpg", "url1", contestBoard));
    files.add(new BoardFile("file2.pdf", "url2", contestBoard));

    // When
    contestBoard.updateFiles(files);

    // Then
    assertThat(contestBoard.getFiles()).hasSize(2);
    assertThat(contestBoard.getFiles().get(0).getName()).isEqualTo("file1.jpg");
    assertThat(contestBoard.getFiles().get(0).getUrl()).isEqualTo("url1");
    assertThat(contestBoard.getFiles().get(1).getName()).isEqualTo("file2.pdf");
    assertThat(contestBoard.getFiles().get(1).getUrl()).isEqualTo("url2");
  }

  @Test
  @DisplayName("ContestBoard 정보를 수정한다.")
  void updateContestBoardInfoTest() {
    // Given
    ContestBoard contestBoard = new ContestBoard();
    Long newContestFieldId = 1L;
    String newTitle = "새로운 제목";
    String newContent = "새로운 내용";
    String newAssociation = "새로운 협회";
    String newTopic = "새로운 주제";
    LocalDate newDateContestStart = LocalDate.now();
    LocalDate newDateContestEnd = LocalDate.now().plusDays(30);

    // When
    contestBoard.updateContest(
        newContestFieldId,
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
}
