package com.inhabas.api.auth.domain.oauth2.member.dto;

import java.util.List;

import lombok.*;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateTypeRequestDto {

  private List<Long> memberIdList;

  private MemberType type;
}
