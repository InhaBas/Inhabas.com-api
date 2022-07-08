package com.inhabas.api.domain.board.domain.valueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Contents {

    @Column(name = "contents", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String value;

    @Transient
    private final int MAX_SIZE = 2 << 24 - 1; //16MB

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
        if (o.isBlank()) return false;
        return o.length() < MAX_SIZE;
    }

    public String getValue() {
        return value;
    }

}
