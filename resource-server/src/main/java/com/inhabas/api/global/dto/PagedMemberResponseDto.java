package com.inhabas.api.global.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class PagedMemberResponseDto<T> {

    @NotNull
    private PageInfoDto pageInfo;

    @NotNull
    private List<T> data;


    @Builder
    public PagedMemberResponseDto(PageInfoDto pageInfo, List<T> data) {
        this.pageInfo = pageInfo;
        this.data = data;
    }

}
