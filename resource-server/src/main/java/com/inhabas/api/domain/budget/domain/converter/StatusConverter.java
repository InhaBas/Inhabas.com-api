package com.inhabas.api.domain.budget.domain.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;

@Converter
public class StatusConverter implements AttributeConverter<ApplicationStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ApplicationStatus status) {

    return status.getValue();
  }

  @Override
  public ApplicationStatus convertToEntityAttribute(Integer dbData) {

    for (ApplicationStatus status : ApplicationStatus.values()) {
      if (Objects.equals(status.getValue(), dbData)) return status;
    }
    return ApplicationStatus.WAITING;
  }
}
