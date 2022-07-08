package com.inhabas.api.domain.team.domain.valueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class TeamName {

    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String value;

    @Transient
    private final int MAX_LENGTH = 20;

    public TeamName() {}

    public TeamName(String value) {
        if (this.validate(value))
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
