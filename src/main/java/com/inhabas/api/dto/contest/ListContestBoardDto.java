package com.inhabas.api.dto.contest;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ListContestBoardDto {
    private String title;
    private String topic;
    private LocalDate start;
    private LocalDate deadline;
}
