package com.inhabas.api.domain.scholarship.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ScholarshipTest {

  @Mock private Menu menu;

  @Mock private Member writer;

  private Scholarship scholarship;

  @BeforeEach
  public void setUp() {
    String title = "title";
    String content = "content";
    LocalDateTime dateHistory = LocalDateTime.now();
    scholarship = new Scholarship(title, menu, content, dateHistory);
  }

  @DisplayName("올바른 Scholarship 를 생성한다.")
  @Test
  public void ConstructorTest() {
    // then
    Assertions.assertThat(scholarship.getTitle()).isEqualTo("title");
    Assertions.assertThat(scholarship.getContent()).isEqualTo("content");
  }

  @DisplayName("Scholarship text 부분을 수정한다.")
  @Test
  public void updateTextTest() {
    // given
    String newTitle = "newTitle";
    String newContent = "newContent";
    LocalDateTime newDateHistory = LocalDateTime.now();

    // when
    scholarship.updateText(newTitle, newContent, newDateHistory);

    // then
    Assertions.assertThat(scholarship.getTitle()).isEqualTo(newTitle);
    Assertions.assertThat(scholarship.getContent()).isEqualTo(newContent);
  }

  @DisplayName("Scholarship file 부분을 수정한다.")
  @Test
  public void updateFilesTest() {
    // given
    List<BoardFile> files = new ArrayList<>();
    BoardFile file = new BoardFile("random", "fileName", "/hello", writer, 10L, "image/jpeg");
    files.add(file);

    // when
    scholarship.updateFiles(files);

    // then
    Assertions.assertThat(scholarship.getFiles().get(0)).isEqualTo(file);
  }
}
