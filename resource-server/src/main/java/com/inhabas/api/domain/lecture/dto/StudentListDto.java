package com.inhabas.api.domain.lecture.dto;

import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class StudentListDto {

    private String name;

    private Integer memberId;

    private String phone;

    private String email;

    private Integer assignmentCount;

    private Integer attendanceCount;

    private StudentStatus status;

    private Integer sid;

    @Builder
    public StudentListDto(String name, Integer memberId, String phone, String email, Integer assignmentCount, Integer attendanceCount, StudentStatus status, Integer sid) {
        this.name = name;
        this.memberId = memberId;
        this.phone = phone;
        this.email = email;
        this.assignmentCount = assignmentCount;
        this.attendanceCount = attendanceCount;
        this.status = status;
        this.sid = sid;
    }
}
