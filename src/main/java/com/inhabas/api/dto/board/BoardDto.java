package com.inhabas.api.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer menuId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private LocalDateTime updated;
}
