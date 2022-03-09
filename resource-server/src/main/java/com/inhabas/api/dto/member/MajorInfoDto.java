package com.inhabas.api.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MajorInfoDto {

    private Integer id;

    private String college;

    private String major;

    public MajorInfoDto(Integer id, String college, String major) {
        this.id = id;
        this.college = college;
        this.major = major;
    }
}
