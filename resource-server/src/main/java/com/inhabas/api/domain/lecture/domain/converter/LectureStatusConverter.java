package com.inhabas.api.domain.lecture.domain.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;

@Converter
public class LectureStatusConverter implements AttributeConverter<LectureStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(LectureStatus status) {

    return status.getValue();
  }

  @Override
  public LectureStatus convertToEntityAttribute(Integer dbData) {

    for (LectureStatus status : LectureStatus.values()) {
      if (Objects.equals(status.getValue(), dbData)) return status;
    }
    return LectureStatus.WAITING;
  }
}
