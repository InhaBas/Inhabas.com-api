package com.inhabas.api.domain.file.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import org.mockito.Mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardFileTest {

  @Mock private Member uploader;

  @DisplayName("Board 에 첨부되는 File 을 생성한다.")
  @Test
  void constructorTest() {
    // given
    String name = "fileName";
    String url = "fileUrl";

    // when
    BoardFile boardFile = new BoardFile("random", name, url, uploader, 10L, "image/jpeg");

    // then
    assertThat(boardFile.getName()).isEqualTo(name);
    assertThat(boardFile.getUrl()).isEqualTo(url);
  }
}
