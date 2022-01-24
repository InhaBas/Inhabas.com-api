package com.inhabas.api.dto.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveBoardDto {
    private String title;
    private String contents;

    public SaveBoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
