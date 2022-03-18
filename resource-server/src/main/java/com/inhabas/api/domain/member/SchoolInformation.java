package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.MemberType;
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
    private Generation generation;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private SchoolInformation(String major, Integer generation, MemberType memberType) {
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
        return major.getValue();
    }

    public Integer getGeneration() {
        return generation.getValue();
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
