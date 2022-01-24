package com.inhabas.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {
    private Integer id;
    private String title;
    private String contents;
    private String writerName;
    private Integer menuId;
    private LocalDateTime created;
    private LocalDateTime updated;

    public BoardDto(Integer id, String title, String contents, String writerName, Integer menuId, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writerName = writerName;
        this.menuId = menuId;
        this.created = created;
        this.updated = updated;
    }
}
