package com.inhabas.api.dto.signUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailSignUpDto {

    private String name;

    private String major;

    private String phoneNumber;

    private String email;

    private Integer memberId;

    private Integer semester;

    @Builder
    public DetailSignUpDto(String name, String major, String phoneNumber, String email, Integer memberId, Integer semester) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberId = memberId;
        this.semester = semester;
    }
}
