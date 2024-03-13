package com.inhabas.api.domain.project.domain;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.domain.menu.domain.Menu;

public class ProjectBoardExampleTest {

  public static ProjectBoard getBoard1(Menu menu) {
    return new ProjectBoard("알파 테스터 제목", menu, "알파테스터 내용", false, null);
  }

  public static ProjectBoard getBoard2(Menu menu) {
    return new ProjectBoard("베타 테스터 제목", menu, "베타테스터 내용", false, null);
  }

  public static ProjectBoard getTestBoard(Integer id, Menu menu) {

    ProjectBoard board = new ProjectBoard("테스트 제목", menu, "테스트 내용", false, null);
    ReflectionTestUtils.setField(board, "id", id);
    return board;
  }
}
