package com.inhabas.api.dto.contest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SaveContestBoardDto {

    @NotBlank(message = "제목을 입력하세요.")
    @Length(max = 100, message = "100자 이하를 입력하세요.")
    private String title;

    @NotBlank(message = "본문은 입력하세요.")
    private String contents;

    @NotBlank(message = "협회기관을 입력하세요.")
    private String association;

    @NotBlank(message = "공모전 주제를 입력하세요.")
    private String topic;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate deadline;

    @NotNull
    private Integer loginedUser;
}
