package com.inhabas.api.dto.contest;

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

    private LocalDate start;
    private LocalDate deadline;

    private LocalDateTime created;
    private LocalDateTime updated;
}
