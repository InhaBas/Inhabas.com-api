package com.inhabas.api.dto.contest;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ListContestDto {
    private String title;
    private String topic;
    private LocalDate start;
    private LocalDate deadline;
}
