package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.member.type.wrapper.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardDto {
    private Integer id;
    private String title;
    private String contents; // BoardListView에서 null
    private String writerName;
    private Integer menuId;

    private LocalDateTime created;
    private LocalDateTime updated;


    public void setTitle(Title title) {
        this.title = title.getValue();
    }

    public void setContents(Contents contents) {
        this.contents = contents.getValue();
    }

    public void setWriterName(Name name) {
        this.writerName = name.getValue();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }
}
