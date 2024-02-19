package com.inhabas.api.global.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class PagedPinnedResponseDto<T> {

    @NotNull
    private PageInfoDto pageInfo;

    private List<T> pinnedData;

    @NotNull private List<T> data;

    @Builder
    public PagedPinnedResponseDto(PageInfoDto pageInfo, List<T> pinnedData, List<T> data) {
        this.pageInfo = pageInfo;
        this.pinnedData = pinnedData;
        this.data = data;
    }
}
