package com.inhabas.api.domain.member;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolInformation {

    @Enumerated(EnumType.STRING)
    private Major major;

    private Integer grade;

    private Integer gen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolInformation)) return false;
        SchoolInformation that = (SchoolInformation) o;
        return getMajor() == that.getMajor()
                && getGrade().equals(that.getGrade())
                && getGen().equals(that.getGen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMajor(), getGrade(), getGen());
    }

}
