package com.inhabas.api.security.domain.socialAccount.type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class UID {

    @Column(name = "uid", nullable = false)
    private String value;

    @Transient
    private final int MAX_SIZE = 191;

    public UID() {}

    public UID(String value) {
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
        return o.length() <= MAX_SIZE;
    }

    public String getValue() {
        return value;
    }
}
