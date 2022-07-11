package com.inhabas.api.domain.budget.converter;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class StatusConverter implements AttributeConverter<ApplicationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ApplicationStatus status) {

        return status.getValue();
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(Integer dbData) {

        for (ApplicationStatus status: ApplicationStatus.values()){
            if (Objects.equals(status.getValue(), dbData))
                return status;
        }
        return ApplicationStatus.WAITING;
    }
}
