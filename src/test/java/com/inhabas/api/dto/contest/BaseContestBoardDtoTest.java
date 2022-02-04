package com.inhabas.api.dto.contest;

import java.time.LocalDate;

public class BaseContestBoardDtoTest {
    public static final SaveContestBoardDto saveContestBoardDto1 = new SaveContestBoardDto(
            "title", "contents", "association", "topic"
            , LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) , 12201863);

    public static final SaveContestBoardDto saveContestBoardDto2 = new SaveContestBoardDto(
            "title", " ", "association", "topic"
            , LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) , 12201863);

    public static final SaveContestBoardDto saveContestBoardDto3 = new SaveContestBoardDto(
            "title".repeat(20) + ".", "contents", "association", "topic"
            , LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) , 12201863);


    public static final UpdateContestBoardDto updateContestBoardDto1 = new UpdateContestBoardDto(
            1, "title", "contents", "association", "topic"
            , LocalDate.of(2022, 01, 02), LocalDate.of(2022,01, 29));

    // Content Is NUll Test
    public static final UpdateContestBoardDto updateContestBoardDto2 = new UpdateContestBoardDto(
            1, "title", " ", "association", "topic"
            , LocalDate.of(2022, 01, 02), LocalDate.of(2022,01, 29));
}
