package com.inhabas.api.dto.contest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DetailContestBoardDto {

    private Integer id;
    private String writerName;
    private String title;
    private String contents;
    private String association;
    private String topic;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyy-MM-dd")
    private LocalDate start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyy-MM-dd")
    private LocalDate deadline;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updated;
}
