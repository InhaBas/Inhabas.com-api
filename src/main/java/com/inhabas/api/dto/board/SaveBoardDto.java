package com.inhabas.api.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
<<<<<<< HEAD
=======
        this.categoryId = categoryId;

>>>>>>> f9c4bbf ([bugfix] GitHub Comments 반영)
    }

}
