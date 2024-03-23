package com.inhabas.api.domain.scholarship.domain;

import java.time.LocalDateTime;

import com.inhabas.api.domain.menu.domain.Menu;

public class ScholarshipBoardExampleTest {

  public static Scholarship getBoard1(Menu menu) {
    return new Scholarship("제목1", menu, "내용1", LocalDateTime.now());
  }

  public static Scholarship getBoard2(Menu menu) {
    return new Scholarship("제목2", menu, "내용2", LocalDateTime.now());
  }
}
