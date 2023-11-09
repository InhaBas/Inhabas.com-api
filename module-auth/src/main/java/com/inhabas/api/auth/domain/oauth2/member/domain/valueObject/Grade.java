package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
public class Grade {

    @Column(name = "GRADE")
    private Integer value;

    public  Grade() {}

    public Grade(Integer value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof Integer))  return false;
        int o = (Integer) value;
        return 0 <= o && o <= 5; // 1학년부터 5학년(초과학기)까지 가능, 0학년은 학생이 아닐때
    }

}
