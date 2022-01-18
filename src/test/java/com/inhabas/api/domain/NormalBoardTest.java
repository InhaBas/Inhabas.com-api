package com.inhabas.api.domain;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class NormalBoardTest {

    public static NormalBoard getFreeBoard() {
        return new NormalBoard("이건 제목", "이건 내용입니다.");
    }
    public static NormalBoard getNoticeBoard1() {
        return new NormalBoard("이건 공지", "이건 공지입니다.");
    }
    public static NormalBoard getNoticeBoard2() {
        return new NormalBoard("이건 공지2", "이건 공지2입니다.");
    }

}
