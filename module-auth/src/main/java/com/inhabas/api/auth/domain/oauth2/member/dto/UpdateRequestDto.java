package com.inhabas.api.auth.domain.oauth2.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateRequestDto {

  private List<Long> memberIdList;

  @Schema(example = "pass, fail")
  private String state;
}
