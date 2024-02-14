package com.inhabas.api.domain.normalBoard.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class NormalBoardTest {

  @Mock
  private Menu menu;

  private NormalBoard normalBoard;

  @BeforeEach
  public void setUp() {

    MockitoAnnotations.openMocks(this);

    String title = "title1";
    String content = "content1";
    normalBoard = new NormalBoard(title, menu, content, false);
  }

  @DisplayName("올바른 NormalBoard 를 생성한다.")
  @Test
  public void ConstructorTest() {
    // then
    Assertions.assertThat(normalBoard.getTitle()).isEqualTo("title1");
    Assertions.assertThat(normalBoard.getContent()).isEqualTo("content1");
  }

  @DisplayName("NormalBoard text 부분을 수정한다.")
  @Test
  public void updateTextTest() {
    // given
    String newTitle = "newTitle";
    String newContent = "newContent";

    // when
    normalBoard.updateText(newTitle, newContent);

    // then
    Assertions.assertThat(normalBoard.getTitle()).isEqualTo(newTitle);
    Assertions.assertThat(normalBoard.getContent()).isEqualTo(newContent);
  }

  @DisplayName("NormalBoard file 부분을 수정한다.")
  @Test
  public void updateFilesTest() {
    // given
    List<BoardFile> files = new ArrayList<>();
    BoardFile file = new BoardFile("fileName", "/hello", normalBoard);
    files.add(file);

    // when
    normalBoard.updateFiles(files);

    // then
    Assertions.assertThat(normalBoard.getFiles().get(0)).isEqualTo(file);
  }
}
