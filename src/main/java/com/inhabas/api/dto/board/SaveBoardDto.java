package com.inhabas.api.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class SaveBoardDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    @NotNull
    private Integer menuId;

    @NotNull
    private Integer loginedUser;

    public SaveBoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Object toEntity() {
        return null;
    }
}
