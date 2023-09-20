package com.inhabas.api.global.util;

import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageUtil {

    public static PagedResponseDto getPagedResponse(Page<?> data) {

        PageInfoDto pageInfoDto = new PageInfoDto(data);
        List<?> content = data.getContent();
        PagedResponseDto pagedResponseDto = new PagedResponseDto(pageInfoDto, content);
        return pagedResponseDto;
    }
}
