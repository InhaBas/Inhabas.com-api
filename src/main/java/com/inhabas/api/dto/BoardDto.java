package com.inhabas.api.dto;

import com.inhabas.api.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDto {
    private String title;
    private String contents;
    private Member writer;

    public BoardDto(String title, String contents, Member writer) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }
}
