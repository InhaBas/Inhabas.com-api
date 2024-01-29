package com.inhabas.api.domain.lecture.domain.converter;

import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import java.util.Objects;
import javax.persistence.AttributeConverter;

public class StudentStatusConverter implements AttributeConverter<StudentStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(StudentStatus attribute) {

    return attribute.getValue();
  }

  @Override
  public StudentStatus convertToEntityAttribute(Integer dbData) {

    for (StudentStatus status : StudentStatus.values()) {
      if (Objects.equals(status.getValue(), dbData)) return status;
    }
    return StudentStatus.PROGRESS;
  }
}
