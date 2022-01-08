package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Grade;
import com.inhabas.api.domain.member.type.wrapper.Major;
import com.inhabas.api.domain.member.type.wrapper.Semester;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class SchoolInformation {

    @Enumerated(EnumType.STRING)
    private Major major;

    @Embedded
    private Grade grade;

    @Embedded
    private Semester gen;

    public SchoolInformation(Major major, Integer grade, Integer semester) {
        this.major = major;
        this.grade = new Grade(grade);
        this.gen = new Semester(semester);
    }

    public Major getMajor() {
        return major;
    }

    public Integer getGrade() {
        return grade.getValue();
    }

    public Integer getGen() {
        return gen.getValue();
    }

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
