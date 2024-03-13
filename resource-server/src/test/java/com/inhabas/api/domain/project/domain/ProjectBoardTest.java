package com.inhabas.api.domain.project.domain;

import java.util.ArrayList;
import java.util.List;

import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProjectBoardTest {

  @Mock private Menu menu;

  private ProjectBoard projectBoard;

  @BeforeEach
  public void setUp() {

    MockitoAnnotations.openMocks(this);

    String title = "title1";
    String content = "content1";
    projectBoard = new ProjectBoard(title, menu, content, false, null);
  }

  @DisplayName("올바른 ProjectBoard 를 생성한다.")
  @Test
  public void ConstructorTest() {
    // then
    Assertions.assertThat(projectBoard.getTitle()).isEqualTo("title1");
    Assertions.assertThat(projectBoard.getContent()).isEqualTo("content1");
  }

  @DisplayName("ProjectBoard text 부분을 수정한다.")
  @Test
  public void updateTextTest() {
    // given
    String newTitle = "newTitle";
    String newContent = "newContent";

    // when
    projectBoard.updateText(newTitle, newContent);

    // then
    Assertions.assertThat(projectBoard.getTitle()).isEqualTo(newTitle);
    Assertions.assertThat(projectBoard.getContent()).isEqualTo(newContent);
  }

  @DisplayName("NormalBoard file 부분을 수정한다.")
  @Test
  public void updateFilesTest() {
    // given
    List<BoardFile> files = new ArrayList<>();
    BoardFile file = new BoardFile("fileName", "/hello", projectBoard);
    files.add(file);

    // when
    projectBoard.updateFiles(files);

    // then
    Assertions.assertThat(projectBoard.getFiles().get(0)).isEqualTo(file);
  }
}
