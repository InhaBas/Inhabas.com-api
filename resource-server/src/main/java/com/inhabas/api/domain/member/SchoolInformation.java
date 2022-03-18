package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Grade;
import com.inhabas.api.domain.member.type.wrapper.Major;
import com.inhabas.api.domain.member.type.wrapper.Generation;
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
    private Generation generation;

    private Boolean isProfessor;

    /* for creating student information*/
    private SchoolInformation(String major, Integer grade, Integer generation) {
        this.major = new Major(major);
        this.grade = new Grade(grade);
        this.generation = new Generation(generation);
        this.isProfessor = false;
    }

    /* for creating professor information */
    private SchoolInformation(String major) {
        this.major = new Major(major);
        this.grade = null;
        this.generation = null;
        this.isProfessor = true;
    }

    public static SchoolInformation ofStudent(String major, Integer grade, Integer generation) {
        return new SchoolInformation(major, grade, generation);
    }

    public static SchoolInformation ofProfessor(String major) {
        return new SchoolInformation(major);
    }

    public String getMajor() {
        return major.getValue();
    }

    public Integer getGeneration() {
        return generation.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchoolInformation)) return false;
        SchoolInformation that = (SchoolInformation) o;
        return Objects.equals(getMajor(), that.getMajor())
                && getGeneration().equals(that.getGeneration());
    }

}
