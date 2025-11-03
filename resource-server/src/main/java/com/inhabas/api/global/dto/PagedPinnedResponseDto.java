package com.inhabas.api.global.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagedPinnedResponseDto<T> {

  @NotNull private PageInfoDto pageInfo;

  private List<T> pinnedData;

  @NotNull private List<T> data;

  @Builder
  public PagedPinnedResponseDto(PageInfoDto pageInfo, List<T> pinnedData, List<T> data) {
    this.pageInfo = pageInfo;
    this.pinnedData = pinnedData;
    this.data = data;
  }
}
