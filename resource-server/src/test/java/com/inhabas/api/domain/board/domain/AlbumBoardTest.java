package com.inhabas.api.domain.board.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

class AlbumBoardTest {

    @Mock
    private Menu menu;

    @Mock
    private Member writer;

    private AlbumBoard albumBoard;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        String title = "title1";
        String content = "content1";
        albumBoard = new AlbumBoard(title, writer, menu, content);

    }

    @DisplayName("올바른 AlbumBoard 를 생성한다.")
    @Test
    public void ConstructorTest() {
        //then
        Assertions.assertThat(albumBoard.getTitle()).isEqualTo("title1");
        Assertions.assertThat(albumBoard.getContent()).isEqualTo("content1");

    }

    @DisplayName("AlbumBoard text 부분을 수정한다.")
    @Test
    public void updateTextTest() {
        //given
        String newTitle = "newTitle";
        String newContent = "newContent";

        //when
        albumBoard.updateText(newTitle, newContent);

        //then
        Assertions.assertThat(albumBoard.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(albumBoard.getContent()).isEqualTo(newContent);

    }

    @DisplayName("AlbumBoard file 부분을 수정한다.")
    @Test
    public void updateFilesTest() {
        //given
        List<BoardFile> files = new ArrayList<>();
        BoardFile file = new BoardFile("fileName", "/hello", albumBoard);
        files.add(file);

        //when
        albumBoard.updateFiles(files);

        //then
        Assertions.assertThat(albumBoard.getFiles().get(0)).isEqualTo(file);

    }


}