package com.inhabas.api.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private String contents;
    private String writerName;
    private Integer menuId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyy-MM-dd'T'HH:mm:ss.SSS")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
