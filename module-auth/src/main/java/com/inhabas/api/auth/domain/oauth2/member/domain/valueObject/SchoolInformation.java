package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject.Major;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolInformation {

    @Embedded
    private Major major;

    @Embedded
    private Generation generation;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private static final int DEFAULT_GRADE = 0; // 학생이 아닌 경우 0학년

    public SchoolInformation(String major, Integer generation, MemberType memberType) {
        this.major = new Major(major);
        this.generation = new Generation(generation);
        this.memberType = memberType;
    }


    /* factory methods */

    public static SchoolInformation ofUnderGraduate(String major, Integer generation) {
        return new SchoolInformation(major, generation, MemberType.UNDERGRADUATE);
    }

    public static SchoolInformation ofProfessor(String major, Integer generation) {
        return new SchoolInformation(major, generation, MemberType.PROFESSOR);
    }

    public static SchoolInformation ofGraduated(String major, Integer generation) {
        return new SchoolInformation(major, generation, MemberType.GRADUATED);
    }

    public static SchoolInformation ofBachelor(String major, Integer generation) {
        return new SchoolInformation(major, generation, MemberType.BACHELOR);
    }

    public static SchoolInformation ofOther(String major, Integer generation) {
        return new SchoolInformation(major, generation, MemberType.OTHER);
    }

    public String getMajor() {
        return this.major.getValue();
    }

    public Integer getGeneration() {
        return this.generation.getValue();
    }

    public MemberType getMemberType() {
        return this.memberType;
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
