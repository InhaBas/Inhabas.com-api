package com.inhabas.api.domain.normalBoard.domain;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.domain.menu.domain.Menu;

public class NormalBoardExampleTest {

  public static NormalBoard getBoard1(Menu menu) {
    return new NormalBoard("이건 공지1", menu, "이건 공지1입니다.", false, null);
  }

  public static NormalBoard getBoard2(Menu menu) {
    return new NormalBoard("이건 공지2", menu, "이건 공지2입니다.", false, null);
  }

  public static NormalBoard getBoard3(Menu menu) {
    return new NormalBoard("이건 공지3", menu, "이건 공지3입니다.", false, null);
  }

  public static NormalBoard getTestBoard(Integer id, Menu menu) {

    NormalBoard board = new NormalBoard("이건 공지", menu, "이건 공지입니다.", false, null);
    ReflectionTestUtils.setField(board, "id", id);
    return board;
  }
}
