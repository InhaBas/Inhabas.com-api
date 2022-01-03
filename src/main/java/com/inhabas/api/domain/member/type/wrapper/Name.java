package com.inhabas.api.domain.member.type.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Name {

    @Column(name = "name")
    private String value;

    @Transient
    private final int MAX_LENGTH = 50;

    public Name() {}

    public Name(String value) {
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
