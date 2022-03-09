package com.inhabas.api.domain.member.type.wrapper;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class Semester {

    @Column(name = "gen")
    private int value;

    public Semester() {}

    public Semester(int value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof Integer)) return false;
        int o = (Integer) value;
        return o == 1 || o == 2;  // 1학기 또는 2학기 밖에 없음.
    }
}
