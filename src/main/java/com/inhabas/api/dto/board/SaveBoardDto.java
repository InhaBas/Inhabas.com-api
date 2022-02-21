package com.inhabas.api.dto.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveBoardDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Length(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    @NotNull
    private Integer menuId;

    @NotNull(message = "로그인 후 이용해주세요.")
    private Integer loginedUser;

    public SaveBoardDto(String title, String contents, Integer menuId, Integer loginedUser) {
        this.title = title;
        this.contents = contents;
        this.menuId = menuId;
        this.loginedUser = loginedUser;
    }

    public SaveBoardDto(Map<String, Object> saveBoard) {
        this.title = saveBoard.get("title").toString();
        this.contents = saveBoard.get("contents").toString();
        this.menuId = (Integer) saveBoard.get("menuId");
        this.loginedUser = (Integer) saveBoard.get("loginedUser");
}
}
