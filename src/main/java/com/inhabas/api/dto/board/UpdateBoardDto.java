package com.inhabas.api.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class UpdateBoardDto {
    @NotNull
    private Integer id;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요")
    private String contents;

    public UpdateBoardDto(Integer id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

}
