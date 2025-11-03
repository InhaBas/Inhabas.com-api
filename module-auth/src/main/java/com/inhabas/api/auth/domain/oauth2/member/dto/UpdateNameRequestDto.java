package com.inhabas.api.auth.domain.oauth2.member.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class UpdateNameRequestDto {

  @NotNull private Long id;

  @NotBlank private String studentId;
  @NotBlank private String major;
  @NotBlank private Role role;
  @NotBlank private MemberType type;
  @NotBlank private String beforeName;
  @NotBlank private String afterName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateRequested;

  @NotBlank private RequestStatus status;
  @NotBlank private String rejectReason;

  @Builder
  public UpdateNameRequestDto(
      Long id,
      String studentId,
      String major,
      Role role,
      MemberType type,
      String beforeName,
      String afterName,
      LocalDateTime dateRequested,
      RequestStatus status,
      String rejectReason) {
    this.id = id;
    this.studentId = studentId;
    this.major = major;
    this.role = role;
    this.type = type;
    this.beforeName = beforeName;
    this.afterName = afterName;
    this.dateRequested = dateRequested;
    this.status = status;
    this.rejectReason = rejectReason;
  }
}
