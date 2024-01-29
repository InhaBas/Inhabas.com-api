package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateRoleRequestDto {
  private List<Long> memberIdList;

  private Role role;
}
