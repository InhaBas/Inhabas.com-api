package com.inhabas.api.domain.menu.domain.valueObject;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.List;

import lombok.Getter;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;

@Getter
public enum MenuType {
  // 관리자에 의해 추가, 삭제 불가능한 메뉴,(메뉴 순서와 이름만 변경가능하다.)

  // 동아리 소개
  INTRODUCE(
      List.of(ANONYMOUS), List.of(EXECUTIVES), List.of(ANONYMOUS), List.of(ADMIN), List.of(ADMIN)),
  // 동아리 활동
  ALBUM(
      List.of(ANONYMOUS),
      List.of(SECRETARY, EXECUTIVES),
      List.of(ANONYMOUS),
      List.of(DEACTIVATED),
      List.of(ANONYMOUS)),
  // 명예의 전당
  HALL_OF_FAME(
      List.of(ANONYMOUS), List.of(ADMIN), List.of(ANONYMOUS), List.of(ADMIN), List.of(ADMIN)),
  // 공지사항
  NOTICE(
      List.of(DEACTIVATED),
      List.of(SECRETARY, EXECUTIVES),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 자유게시판
  FREE(
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 질문게시판
  QUESTION(
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 건의 사항
  SUGGEST(
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 공개 자료실
  STORAGE(
      List.of(ANONYMOUS),
      List.of(BASIC),
      List.of(ANONYMOUS),
      List.of(DEACTIVATED),
      List.of(ANONYMOUS)),
  // 회장단 게시판
  EXECUTIVE(
      List.of(SECRETARY, EXECUTIVES),
      List.of(SECRETARY, EXECUTIVES),
      List.of(SECRETARY, EXECUTIVES),
      List.of(SECRETARY, EXECUTIVES),
      List.of(SECRETARY, EXECUTIVES)),
  // 강의
  LECTURE(List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC)),
  // 스터디
  STUDY(List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC)),
  // 취미활동
  HOBBY(List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC), List.of(BASIC)),
  // 대기중인 강의 관리
  LECTURE_PENDING(
      List.of(EXECUTIVES), List.of(ADMIN), List.of(EXECUTIVES), List.of(ADMIN), List.of(ADMIN)),
  // 지원금 신청
  BUDGET_SUPPORT(
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(ADMIN),
      List.of(ADMIN)),
  // 회계 내역
  BUDGET_ACCOUNT(
      List.of(DEACTIVATED),
      List.of(SECRETARY),
      List.of(DEACTIVATED),
      List.of(ADMIN),
      List.of(ADMIN)),
  // 알파테스터
  ALPHA(
      List.of(DEACTIVATED),
      List.of(BASIC),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 베타테스터
  BETA(
      List.of(DEACTIVATED),
      List.of(BASIC),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED),
      List.of(DEACTIVATED)),
  // 공모전
  CONTEST(
      List.of(ANONYMOUS),
      List.of(BASIC),
      List.of(ANONYMOUS),
      List.of(DEACTIVATED),
      List.of(ANONYMOUS)),
  // 대외활동
  ACTIVITY(
      List.of(ANONYMOUS),
      List.of(BASIC),
      List.of(ANONYMOUS),
      List.of(DEACTIVATED),
      List.of(ANONYMOUS)),
  // 장학회
  SCHOLARSHIP(
      List.of(ANONYMOUS),
      List.of(SECRETARY, EXECUTIVES),
      List.of(ANONYMOUS),
      List.of(ADMIN),
      List.of(ADMIN)),
  // 후원 내용
  SPONSOR(
      List.of(ANONYMOUS),
      List.of(SECRETARY, EXECUTIVES),
      List.of(ANONYMOUS),
      List.of(BASIC),
      List.of(ANONYMOUS)),
  // 사용 내역
  USAGE(
      List.of(ANONYMOUS),
      List.of(SECRETARY, EXECUTIVES),
      List.of(ANONYMOUS),
      List.of(BASIC),
      List.of(ANONYMOUS)),

  // 관리자가 추가, 삭제 가능한 메뉴.(NormalBoard 연관)
  LIST(
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS)), // 리스트형 게시판 메뉴
  CARD(
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS),
      List.of(ANONYMOUS)), // 카드형 게시판 메뉴
  ;

  private final List<Role> readBoardListRole;
  private final List<Role> createBoardRole;
  private final List<Role> readBoardRole;
  private final List<Role> createCommentRole;
  private final List<Role> readCommentRole;

  MenuType(
      List<Role> readBoardListRole,
      List<Role> createBoardRole,
      List<Role> readBoardRole,
      List<Role> createCommentRole,
      List<Role> readCommentRole) {
    this.readBoardListRole = readBoardListRole;
    this.createBoardRole = createBoardRole;
    this.readBoardRole = readBoardRole;
    this.createCommentRole = createCommentRole;
    this.readCommentRole = readCommentRole;
  }
}
