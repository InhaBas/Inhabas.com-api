package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.*;

import lombok.*;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MyProfileDto {

  @NotBlank
  @Length(max = 50)
  private String name;

  @NotNull private String studentId;

  @NotNull private String major;

  @NotNull @Positive private Integer grade;

  @Email private String email;

  @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
  private String phoneNumber;

  @NotNull private String picture;

  @NotNull private Role role;

  @NotNull private MemberType type;

  @NotNull
  private Boolean isHOF;

  private String introduce;

  @Builder
  public MyProfileDto(
      String name,
      String studentId,
      String major,
      Integer grade,
      String email,
      String phoneNumber,
      String picture,
      Role role,
      MemberType type,
      Boolean isHOF,
      String introduce) {
    this.name = name;
    this.studentId = studentId;
    this.major = major;
    this.grade = grade;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.picture = picture;
    this.role = role;
    this.type = type;
    this.isHOF = isHOF;
    this.introduce = introduce;
  }
}
