package com.inhabas.api.domain.lecture.dto;

import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class LectureStatusUpdateRequest {

  @NotNull private LectureStatus status;

  @Length(max = 200)
  private String rejectReason;
}
