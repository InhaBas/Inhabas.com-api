package com.inhabas.api.domain.member.type.wrapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
public class Grade {

    @Column(name = "grade")
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
        return 0 < o && o <= 5; // 1학년부터 5학년(초과학기)까지 가능
    }


}
