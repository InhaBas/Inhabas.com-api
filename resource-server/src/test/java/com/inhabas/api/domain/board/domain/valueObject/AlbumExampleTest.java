package com.inhabas.api.domain.board.domain.valueObject;

import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import org.springframework.test.util.ReflectionTestUtils;

public class AlbumExampleTest {

    public static AlbumBoard getAlbumBoard1(Menu menu) {
        return new AlbumBoard(
                "제목1",
                menu,
                "내용1");
    }

    public static AlbumBoard getAlbumBoard2() {
        return new AlbumBoard(
                "제목2",
                new Menu(new MenuGroup("IBAS"),2, MenuType.ALBUM,"동아리 활동", "설명"),
                "내용2");
    }


    public static AlbumBoard getAlbumBoardWithId(Long id) {
        AlbumBoard board = new AlbumBoard(
                "제목2",
                new Menu(new MenuGroup("IBAS"),2, MenuType.ALBUM,"동아리 활동", "설명"),
                "내용2");
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

}
