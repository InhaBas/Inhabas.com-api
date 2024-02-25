package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class HallOfFameDto {

  @NotBlank
  @Length(max = 50)
  private String name;

  @NotNull @Positive private Long memberId;

  @NotNull private String studentId;

  @NotNull private Integer generation;

  @NotBlank
  @Length(max = 50)
  private String major;

  private String picture;

  @NotNull private String intro;

  @NotNull @Email private String email;

  @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
  @Schema(example = "010-0101-0101")
  private String phoneNumber;

  @Builder
  public HallOfFameDto(
      String name,
      Long memberId,
      String studentId,
      Integer generation,
      String major,
      String picture,
      String intro,
      String email,
      String phoneNumber) {
    this.name = name;
    this.memberId = memberId;
    this.studentId = studentId;
    this.generation = generation;
    this.major = major;
    this.picture = picture;
    this.intro = intro;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}
