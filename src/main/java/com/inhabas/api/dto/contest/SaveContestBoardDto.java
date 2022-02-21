package com.inhabas.api.dto.contest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SaveContestBoardDto {

    @NotBlank(message = "제목을 입력하세요.")
    @Length(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    @NotNull(message = "메뉴를 선택해주세요.")
    private Integer menuId;

    @Length(max = 100, message = "100자 이내로 작성해주세요.")
    @NotBlank(message = "협회기관을 입력하세요.")
    private String association;

    @Length(max = 500, message = "500자 이내로 작성해주세요.")
    @NotBlank(message = "공모전 주제를 입력하세요.")
    private String topic;

    @NotNull(message = "공모전 모집 시작일을 등록해주세요.")
    private LocalDate start;

    @NotNull(message = "공모전 모집 마감일을 등록해주세요.")
    @Future (message = "이미 모집기간이 종료된 공모전은 등록할 수 없습니다.")
    private LocalDate deadline;

    @NotNull(message = "로그인 후 이용해주세요.")
    private Integer loginedUser;

    public SaveContestBoardDto(Map<String, Object> saveBoard) {
        this.title = saveBoard.get("title").toString();
        this.contents = saveBoard.get("contents").toString();
        this.menuId = (Integer) saveBoard.get("menuId");
        this.loginedUser = (Integer) saveBoard.get("loginedUser");
        this.association = saveBoard.get("association").toString();
        this.topic = saveBoard.get("topic").toString();
        this.start = LocalDate.parse(saveBoard.get("start").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.deadline = LocalDate.parse(saveBoard.get("deadline").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
