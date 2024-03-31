package com.inhabas.api.domain.contest.domain;

import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.domain.menu.domain.Menu;

public class ContestBoardExampleTest {
  public static ContestBoard getBoard1(Menu menu, ContestField contestField) {
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목1")
            .content("테스트 내용1")
            .association("(주) 아이바스1")
            .topic("테스트 주제1")
            .dateContestStart(LocalDateTime.now())
            .dateContestEnd(LocalDateTime.now().plusDays(10))
            .build();
    return contestBoard;
  }

  public static ContestBoard getBoard2(Menu menu, ContestField contestField) {
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목2")
            .content("테스트 내용2")
            .association("(주) 아이바스2")
            .topic("테스트 주제2")
            .dateContestStart(LocalDateTime.now())
            .dateContestEnd(LocalDateTime.now().plusDays(10))
            .build();
    return contestBoard;
  }

  public static ContestBoard getBoard3(Menu menu, ContestField contestField) {
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목3")
            .content("테스트 내용3")
            .association("(주) 아이바스3")
            .topic("테스트 주제3")
            .dateContestStart(LocalDateTime.now())
            .dateContestEnd(LocalDateTime.now().plusDays(10))
            .build();
    return contestBoard;
  }

  public static ContestBoard getTestBoard(Integer id, Menu menu, ContestField contestField) {

    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title("테스트 제목1")
            .content("테스트 내용1")
            .association("(주) 아이바스1")
            .topic("테스트 주제1")
            .dateContestStart(LocalDateTime.now())
            .dateContestEnd(LocalDateTime.now().plusDays(10))
            .build();
    ReflectionTestUtils.setField(contestBoard, "id", id);
    return contestBoard;
  }
}
