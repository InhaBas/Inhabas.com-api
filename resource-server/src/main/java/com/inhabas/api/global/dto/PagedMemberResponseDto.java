package com.inhabas.api.global.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagedMemberResponseDto<T> {

  @NotNull private PageInfoDto pageInfo;

  @NotNull private List<T> data;

  @Builder
  public PagedMemberResponseDto(PageInfoDto pageInfo, List<T> data) {
    this.pageInfo = pageInfo;
    this.data = data;
  }
}
