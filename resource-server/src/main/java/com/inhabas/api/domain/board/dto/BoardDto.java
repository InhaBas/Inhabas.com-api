package com.inhabas.api.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardDto {
    private Integer id;
    private String title;
    private String contents;
    private String writerName;
    private MenuId menuId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private LocalDateTime updated;
}
