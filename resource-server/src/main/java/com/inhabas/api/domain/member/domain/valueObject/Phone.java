package com.inhabas.api.domain.member.domain.valueObject;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@Getter
public class Phone {

    @Column(name = "PHONE", nullable = false, length = 15)
    private String value;

    public Phone() {}

    public Phone(String value) {
        if (this.validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String)) return false;
        String o = (String) value;
        return Pattern.compile("^(010)-\\d{4}-\\d{4}$").matcher(o).matches();  // 010-xxxx-xxxx 형태만 받음.
    }
}
