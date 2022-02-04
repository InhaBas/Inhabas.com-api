package com.inhabas.api.dto.contest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SaveContestBoardDto {

    @NotBlank(message = "제목을 입력하세요.")
    @Length(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
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
