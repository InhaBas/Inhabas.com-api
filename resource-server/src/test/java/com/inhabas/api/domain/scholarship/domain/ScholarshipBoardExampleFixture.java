package com.inhabas.api.domain.scholarship.domain;

import static com.inhabas.api.auth.testFixture.TestTimeFixture.FIXED_TIME;

import com.inhabas.api.domain.menu.domain.Menu;

public class ScholarshipBoardExampleFixture {

  public static Scholarship getBoard1(Menu menu) {
    return new Scholarship("제목1", menu, "내용1", FIXED_TIME);
  }

  public static Scholarship getBoard2(Menu menu) {
    return new Scholarship("제목2", menu, "내용2", FIXED_TIME.plusHours(1));
  }
}
