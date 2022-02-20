package com.inhabas.api.domain.menu;

public enum MenuType {
    // 관리자에 의해 추가, 삭제 불가능한 메뉴,(메뉴 순서와 이름만 변경가능하다.)
    INTRODUCE,
    ALBUM,
    BUDGET_ACCOUNT,
    BUDGET_SUPPORT,
    GRADUATED,
    CONTEST,
    LECTURE,
    STUDY,
    HOBBY,

    // 추가 삭제 가능한 메뉴. (NormalBoard 연관)
    LIST,       // 리스트형 게시판 메뉴
    CARD,       // 카드형 게시판 메뉴
}
