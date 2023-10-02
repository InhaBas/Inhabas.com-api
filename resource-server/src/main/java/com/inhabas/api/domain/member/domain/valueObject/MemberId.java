package com.inhabas.api.domain.member.domain.valueObject;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberId implements Serializable {

    private static final long serialVersionUID = -2924578165705238561L;

    @Column(name = "STUDENT_ID")
    private Integer id;

    public MemberId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberId memberId = (MemberId) o;
        return this.id.equals(memberId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    public Integer getValue() {
        return id;
    }
}
