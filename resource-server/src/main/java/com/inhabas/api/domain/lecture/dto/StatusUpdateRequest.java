package com.inhabas.api.domain.lecture.dto;

import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class StatusUpdateRequest {

    @NotNull
    private LectureStatus status;

    @Length(max = 200)
    private String rejectReason;


}
