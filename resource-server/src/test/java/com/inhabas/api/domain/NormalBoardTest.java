package com.inhabas.api.domain;

import com.inhabas.api.domain.board.NormalBoard;

public class NormalBoardTest {

    public static NormalBoard getBoard1() {
        return new NormalBoard("이건 제목", "이건 내용입니다.");
    }
    public static NormalBoard getBoard2() {
        return new NormalBoard("이건 공지", "이건 공지입니다.");
    }
    public static NormalBoard getBoard3() {
        return new NormalBoard("이건 공지2", "이건 공지2입니다.");
    }

    public static NormalBoard getTestBoard(Integer id) {
        return new NormalBoard(id, "이건 공지2", "이건 공지2입니다.");
    }

}
