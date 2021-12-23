package com.inhabas.api.dto;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDto {
    private String title;
    private String contents;
    private Member writer;
    private Category category;

    public BoardDto(String title, String contents, Member writer, Category category) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.category = category;
    }
}
