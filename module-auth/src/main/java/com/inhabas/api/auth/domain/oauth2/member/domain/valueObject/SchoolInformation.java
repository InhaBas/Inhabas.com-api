package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.util.Objects;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject.Major;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolInformation {

  @Embedded private Major major;

  @Embedded private Grade grade;

  @Embedded private Generation generation;

  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private MemberType memberType;

  private static final int DEFAULT_GRADE = 0; // 학생이 아닌 경우 0학년

  public SchoolInformation(String major, Integer grade, Integer generation, MemberType memberType) {
    this.major = new Major(major);
    this.grade = new Grade(grade);
    this.generation = new Generation(generation);
    this.memberType = memberType;
  }

  public SchoolInformation(String major, Integer generation, MemberType memberType) {
    this.major = new Major(major);
    this.generation = new Generation(generation);
    this.memberType = memberType;
    this.grade = new Grade(DEFAULT_GRADE);
  }

  /* factory methods */
  public static SchoolInformation ofUnderGraduate(String major, Integer generation) {
    return new SchoolInformation(major, generation, MemberType.UNDERGRADUATE);
  }

  public String getMajor() {
    return this.major.getValue();
  }

  public Integer getGrade() {
    return this.grade.getValue();
  }

  public Integer getGeneration() {
    return this.generation.getValue();
  }

  public MemberType getMemberType() {
    return this.memberType;
  }

  public void setMemberType(MemberType memberType) {
    this.memberType = memberType;
  }

  public void setMajor(String major) {
    this.major = new Major(major);
  }

  public void setGrade(Integer grade) {
    this.grade = new Grade(grade);
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
