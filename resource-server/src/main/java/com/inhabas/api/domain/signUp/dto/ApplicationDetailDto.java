package com.inhabas.api.domain.signUp.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ApplicationDetailDto {

  @NotNull @Positive private Long memberId;

  @NotBlank private String studentId;

  @NotBlank private String name;

  @NotNull @Positive private Integer grade;

  @NotBlank private String major;

  @NotBlank @Email private String email;

  @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
  private String phoneNumber;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateJoined;

  @NotNull private List<QuestionAnswerDto> answers;

  @Builder
  public ApplicationDetailDto(
      Long memberId,
      String studentId,
      String name,
      Integer grade,
      String major,
      String email,
      String phoneNumber,
      LocalDateTime dateJoined,
      List<QuestionAnswerDto> answers) {
    this.memberId = memberId;
    this.studentId = studentId;
    this.name = name;
    this.grade = grade;
    this.major = major;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.dateJoined = dateJoined;
    this.answers = answers;
  }
}
