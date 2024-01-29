package com.inhabas.api.auth.domain.oauth2.majorInfo.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject.College;
import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.valueObject.Major;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;

/**
 * 전공 정보는 학교 운영 방침에 따라 변함. 항상 전공 목록을 최신으로 유지하기는 하지만, 과거 변경되기 이전의 전공을 갖는 학생 정보가 남아있어야 한다. <br>
 * 따라서 회원 정보에 전공을 저장한 때는 fk 로 저장하지 않고 단순 문자열로만 저장한다.
 *
 * @see Member
 */
@Entity
@Table(name = "MAJOR")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MajorInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Embedded private College college;

  @Embedded private Major major;

  public MajorInfo(String college, String major) {
    this.college = new College(college);
    this.major = new Major(major);
  }

  public Integer getId() {
    return id;
  }

  public String getCollege() {
    return college.getValue();
  }

  public String getMajor() {
    return major.getValue();
  }
}
