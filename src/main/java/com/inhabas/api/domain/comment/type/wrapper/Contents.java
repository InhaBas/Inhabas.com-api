package com.inhabas.api.domain.comment.type.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Contents {

    @Column(name = "contents")
    private String value;

    @Transient
    private final int MAX_LENGTH = 500;

    public Contents() {}

    public Contents(String value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String))  return false;
        String o = (String) value;
        return o.length() < MAX_LENGTH;
    }

    public String getValue() {
        return value;
    }
}
