package com.inhabas.api.domain.menu.domain.valueObject;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import lombok.Getter;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

@Getter
public enum MenuType {
    // 관리자에 의해 추가, 삭제 불가능한 메뉴,(메뉴 순서와 이름만 변경가능하다.)

    // 동아리 소개
    INTRODUCE(EXECUTIVES, ANONYMOUS, EXECUTIVES, ANONYMOUS),
    // 동아리 활동
    ALBUM(EXECUTIVES, ANONYMOUS, DEACTIVATED, ANONYMOUS),
    // 명예의 전당
    HALL_OF_FAME(EXECUTIVES, ANONYMOUS, DEACTIVATED, ANONYMOUS),
    // 공지사항, 자유게시판 등 공개되는 기본 게시판
    NORMAL(DEACTIVATED, ANONYMOUS, DEACTIVATED, ANONYMOUS),
    // 1:1 건의사항
    NORMAL_PERSONAL(DEACTIVATED, DEACTIVATED, DEACTIVATED, DEACTIVATED),
    // 회장단 게시판
    NORMAL_EXECUTIVES(EXECUTIVES, EXECUTIVES, EXECUTIVES, EXECUTIVES),
    // 강의
    LECTURE(BASIC, BASIC, BASIC, BASIC),
    // 스터디
    STUDY(BASIC, BASIC, BASIC, BASIC),
    // 취미활동
    HOBBY(BASIC, BASIC, BASIC, BASIC),
    // 대기중인 강의 관리
    LECTURE_EXECUTIVES(EXECUTIVES, EXECUTIVES, EXECUTIVES, EXECUTIVES),
    // 지원금 신청
    BUDGET_SUPPORT(SECRETARY, SECRETARY, SECRETARY, SECRETARY),
    // 회계 내역
    BUDGET_ACCOUNT(SECRETARY, SECRETARY, SECRETARY, SECRETARY),
    // 알파테스터, 베타테스터
    TESTER(BASIC, BASIC, BASIC, BASIC),
    // 공모전
    CONTEST(BASIC, BASIC, BASIC, BASIC),

    // 관리자가 추가, 삭제 가능한 메뉴.(NormalBoard 연관)
    LIST(ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS),       // 리스트형 게시판 메뉴
    CARD(ANONYMOUS, ANONYMOUS, ANONYMOUS, ANONYMOUS),       // 카드형 게시판 메뉴
    ;

    private final Role createBoardRole;
    private final Role readBoardRole;
    private final Role createCommentRole;
    private final Role readCommentRole;

    MenuType(Role createBoardRole, Role readBoardRole, Role createCommentRole, Role readCommentRole) {
        this.createBoardRole = createBoardRole;
        this.readBoardRole = readBoardRole;
        this.createCommentRole = createCommentRole;
        this.readCommentRole = readCommentRole;
    }

}
