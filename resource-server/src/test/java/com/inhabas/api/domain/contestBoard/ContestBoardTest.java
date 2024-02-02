package com.inhabas.api.domain.contestBoard;

import java.time.LocalDate;

import com.inhabas.api.domain.contest.domain.ContestBoard;

public class ContestBoardTest {
  public static ContestBoard getContestBoard1() {
    return ContestBoard.builder()
        .title("공공데이터 개방, 활용 가속화 해커톤")
        .contents("[공개해] 공공데이터 개방‧활용 가속화 해커톤 / 해커톤 내용은 다음과 같습니다. Pol, a bene parma, raptus vita")
        .association("행정안전부/NIA/㈜씨에이에스")
        .topic("공공데이터 포털(www.data.go.kr)에 개방된 데이터 및 API를 활용한 아이디어 기획 및 서비스 개발")
        .start(LocalDate.of(2022, 1, 1))
        .deadline(LocalDate.of(2022, 3, 1))
        .build();
  }

  public static ContestBoard getContestBoard2() {
    return ContestBoard.builder()
        .title("This is title")
        .contents("This is contest")
        .association("This is association")
        .topic("This is topic")
        .start(LocalDate.of(2022, 1, 1))
        .deadline(LocalDate.of(2022, 3, 20))
        .build();
  }

  public static ContestBoard getContestBoard3() {
    return ContestBoard.builder()
        .title("조선/해양산업 디지털 혁신을 위한 BIG DATA / AI 대학생 경진대회")
        .contents("■ 1차 서류 접수 기간 : 2020년 12월 14(월) - 2020년 12월 21(목)\n")
        .association("현대 중공업 그룹")
        .topic("빅데이터/AI")
        .start(LocalDate.of(2022, 1, 4))
        .deadline(LocalDate.of(2022, 2, 25))
        .build();
  }
}
