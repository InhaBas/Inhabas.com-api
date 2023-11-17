package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentId {


    @Column(name = "STUDENT_ID", length = 30, nullable = false)
    private String id;

    @Transient
    private static final int MAX_LENGTH = 30;

    public StudentId(String id) {
        if (validate(id))
            this.id = id;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String)) return false;
        String o = (String) value;
        return  o.length() < MAX_LENGTH;
    }

    public String getValue() {
        return id;
    }
}
