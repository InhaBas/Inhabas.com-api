package com.inhabas.api.domain.member.type.wrapper;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Introduce {

    @Column(name = "USER_INTRO")
    private String value;

    @Transient
    private final int MAX_LENGTH = 300;

    public Introduce() {}

    public Introduce(String value) {
        if (Objects.isNull(value))
            this.value = "";
        else if (validate(value))
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
