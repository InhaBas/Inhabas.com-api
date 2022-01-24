package com.inhabas.api.domain.menu.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class MenuName {
    @Column(name = "name")
    private String value;

    @Transient
    private final int MAX_LENGTH = 15;

    public MenuName() {}

    public MenuName(String value) {
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
        return o.length() < MAX_LENGTH;
    }

    public String getValue() {
        return value;
    }
}
