package com.inhabas.api.dto;

import com.inhabas.api.domain.board.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDto {
    private String title;
    private String contents;
    private Integer writerId;
    private Category category;

    public BoardDto(String title, String contents, Integer writerId, Category category) {
        this.title = title;
        this.contents = contents;
        this.writerId = writerId;
        this.category = category;
    }
}
