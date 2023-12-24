package com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Major {

    @Column(name = "MAJOR", length = 50)
    private String value;

    @Transient
    private static final int MAX_LENGTH = 50;

    public Major() {}

    public Major(String value) {
        if (validate(value))
            this.value = value;
        else
            throw new InvalidInputException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String))  return false;

        String o = (String) value;
        if (o.isBlank()) return false;
        return o.length() < MAX_LENGTH;
    }

    public String getValue() {
        return value;
    }
}
