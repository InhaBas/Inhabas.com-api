package com.inhabas.api.global.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class PageInfoDto {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;


    public PageInfoDto(Page<?> page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

}
