package com.inhabas.api.domain.file.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.domain.board.domain.BaseBoard;
import org.mockito.Mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardFileTest {

  @DisplayName("Board 에 첨부되는 File 을 생성한다.")
  @Test
  void constructorTest() {
    // given
    String name = "fileName";
    String url = "fileUrl";
    BaseBoard baseBoard = Mockito.mock(BaseBoard.class);

    // when
    BoardFile boardFile = new BoardFile(name, url, baseBoard);

    // then
    assertThat(boardFile.getName()).isEqualTo(name);
    assertThat(boardFile.getUrl()).isEqualTo(url);
  }
}
