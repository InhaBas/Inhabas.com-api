package com.inhabas.api.domain.member.domain.valueObject;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 회원의 재학중인 학기가 아니라, 동아리 운영 상의 기수를 의미함.
 * 2020-2학기부터 1기, 2021-1학기가 2기,,
 */
@Embeddable
@Getter
public class Generation {

    @Column(name = "GENERATION")
    private int value;

    public Generation() {}

    public Generation(int value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof Integer)) return false;
        int o = (Integer) value;
        return o > 0;
    }
}
