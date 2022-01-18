package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveBoardDto {
    private String title;
    private String contents;
    private Category category;

    public SaveBoardDto(String title, String contents, Category category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

}
