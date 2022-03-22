package com.inhabas.api.domain.member.type.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    @Column(name = "email", length = 150, nullable = false)
    private String value;

    @Transient
    private final int MAX_LENGTH = 150;

    @Transient
    private final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public Email() {}

    public Email(String value) {
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
        return o.length() < MAX_LENGTH && Pattern.compile(regex).matcher(o).matches();
    }

    public String getValue() {
        return value;
    }
}
