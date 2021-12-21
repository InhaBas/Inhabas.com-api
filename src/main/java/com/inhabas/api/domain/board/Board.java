package com.inhabas.api.domain.board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter()
public class Board {

    private Long id;
    private String title;
    private String contents;
    private Integer writerId;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Board() {
    }

    public Board(String title, String contents, Integer writerId) {
        this.title = title;
        this.contents = contents;
        this.writerId = writerId;
    }
}
