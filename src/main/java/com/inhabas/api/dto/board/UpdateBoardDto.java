package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateBoardDto {
    private Integer id;
    private String title;
    private String contents;
    private Category category;

    public UpdateBoardDto(Integer id, String title, String contents, Category category) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.category = category;
    }
}
