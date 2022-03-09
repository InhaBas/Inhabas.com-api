package com.inhabas.api.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MajorInfoSaveDto {

    @NotBlank @Length(max = 20)
    private String college;

    @NotBlank @Length(max = 50)
    private String major;

    public MajorInfoSaveDto(String college, String major) {
        this.college = college;
        this.major = major;
    }
}
