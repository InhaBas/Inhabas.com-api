package com.inhabas.api.dto.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateBoardDto {
    private Integer id;
    private String title;
    private String contents;

    public UpdateBoardDto(Integer id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }
}
