package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Grade;
import com.inhabas.api.domain.member.type.wrapper.Major;
import com.inhabas.api.domain.member.type.wrapper.Semester;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolInformation {

    @Embedded
    private Major major;

    @Embedded
    private Grade grade;

    @Embedded
    private Semester gen;

    private Boolean isProfessor;

    /* for creating student information*/
    private SchoolInformation(String major, Integer grade, Integer semester) {
        this.major = new Major(major);
        this.grade = new Grade(grade);
        this.gen = new Semester(semester);
        this.isProfessor = false;
    }

    /* for creating professor information */
    private SchoolInformation(String major) {
        this.major = new Major(major);
        this.grade = null;
        this.gen = null;
        this.isProfessor = true;
    }

    public static SchoolInformation ofStudent(String major, Integer grade, Integer semester) {
        return new SchoolInformation(major, grade, semester);
    }

    public static SchoolInformation ofProfessor(String major) {
        return new SchoolInformation(major);
    }

    public String getMajor() {
        return major.getValue();
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
        return Objects.equals(getMajor(), that.getMajor())
                && getGrade().equals(that.getGrade())
                && getGen().equals(that.getGen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMajor(), getGrade(), getGen());
    }

}
