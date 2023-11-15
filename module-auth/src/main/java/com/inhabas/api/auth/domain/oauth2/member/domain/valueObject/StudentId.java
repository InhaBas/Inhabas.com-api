package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentId {


    @Column(name = "STUDENT_ID", nullable = false)
    private Integer id;

    public StudentId(Integer id) {
        if (validate(id))
            this.id = id;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof Integer)) return false;
        Integer o = (Integer) value;
        return  0 < o;
    }

    public Integer getValue() {
        return id;
    }
}
