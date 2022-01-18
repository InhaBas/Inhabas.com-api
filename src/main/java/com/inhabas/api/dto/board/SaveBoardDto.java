package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.NormalBoard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
public class SaveBoardDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    public SaveBoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
