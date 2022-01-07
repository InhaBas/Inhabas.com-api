package com.inhabas.api.domain;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;

public class NormalBoardTest {
    public static final NormalBoard FREE_BOARD = new NormalBoard("이건 제목", "이건 내용입니다.", Category.free);
    public static final NormalBoard NOTICE_BOARD = new NormalBoard("이건 공지", "이건 공지입니다.", Category.notice);
    public static final NormalBoard NOTICE_BOARD_2 = new NormalBoard("이건 공지2", "이건 공지2입니다.", Category.notice);

}
