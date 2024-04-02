package com.inhabas.api.domain.menu.domain.valueObject;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import lombok.Getter;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;

@Getter
public enum MenuType {
  // 관리자에 의해 추가, 삭제 불가능한 메뉴,(메뉴 순서와 이름만 변경가능하다.)

  // 동아리 소개
  INTRODUCE(ANONYMOUS, EXECUTIVES, ANONYMOUS, ADMIN, ADMIN),
  // 동아리 활동
  ALBUM(ANONYMOUS, EXECUTIVES, ANONYMOUS, DEACTIVATED, ANONYMOUS),
  // 명예의 전당
  HALL_OF_FAME(ANONYMOUS, ADMIN, ANONYMOUS, ADMIN, ADMIN),
  // 공지사항
  NOTICE(DEACTIVATED, EXECUTIVES, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 자유게시판
  FREE(DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 질문게시판
  QUESTION(DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 건의 사항
  SUGGEST(DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 공개 자료실
  STORAGE(ANONYMOUS, BASIC, ANONYMOUS, DEACTIVATED, ANONYMOUS),
  // 회장단 게시판
  EXECUTIVE(SECRETARY, SECRETARY, SECRETARY, SECRETARY, SECRETARY),
  // 강의
  LECTURE(BASIC, BASIC, BASIC, BASIC, BASIC),
  // 스터디
  STUDY(BASIC, BASIC, BASIC, BASIC, BASIC),
  // 취미활동
  HOBBY(BASIC, BASIC, BASIC, BASIC, BASIC),
  // 대기중인 강의 관리
  LECTURE_PENDING(EXECUTIVES, ADMIN, EXECUTIVES, ADMIN, ADMIN),
  // 지원금 신청
  BUDGET_SUPPORT(DEACTIVATED, DEACTIVATED, DEACTIVATED, ADMIN, ADMIN),
  // 회계 내역
  BUDGET_ACCOUNT(DEACTIVATED, SECRETARY, DEACTIVATED, ADMIN, ADMIN),
  // 알파테스터
  ALPHA(DEACTIVATED, BASIC, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 베타테스터
  BETA(DEACTIVATED, BASIC, DEACTIVATED, DEACTIVATED, DEACTIVATED),
  // 공모전
  CONTEST(ANONYMOUS, BASIC, ANONYMOUS, DEACTIVATED, BASIC),
  // 대외활동
  ACTIVITY(ANONYMOUS, BASIC, ANONYMOUS, DEACTIVATED, BASIC),
  // 장학회
  SCHOLARSHIP(ANONYMOUS, SECRETARY, ANONYMOUS, ADMIN, ADMIN),
  // 후원 내용
  SPONSOR(ANONYMOUS, EXECUTIVES, ANONYMOUS, BASIC, ANONYMOUS),
  // 사용 내역
  USAGE(ANONYMOUS, EXECUTIVES, ANONYMOUS, BASIC, ANONYMOUS),

  // 관리자가 추가, 삭제 가능한 메뉴.(NormalBoard 연관)
  LIST(ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS), // 리스트형 게시판 메뉴
  CARD(ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS), // 카드형 게시판 메뉴
  ;

  private final Role readBoardListRole;
  private final Role createBoardRole;
  private final Role readBoardRole;
  private final Role createCommentRole;
  private final Role readCommentRole;

  MenuType(
      Role readBoardListRole,
      Role createBoardRole,
      Role readBoardRole,
      Role createCommentRole,
      Role readCommentRole) {
    this.readBoardListRole = readBoardListRole;
    this.createBoardRole = createBoardRole;
    this.readBoardRole = readBoardRole;
    this.createCommentRole = createCommentRole;
    this.readCommentRole = readCommentRole;
  }
}
