package com.inhabas.api.domain.comment.domain.valueObject;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Content {

    @Column(name = "content", length = 500, nullable = false)
    private String value;

    @Transient
    private final int MAX_LENGTH = 500;

    public Content() {}

    public Content(String value) {
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
