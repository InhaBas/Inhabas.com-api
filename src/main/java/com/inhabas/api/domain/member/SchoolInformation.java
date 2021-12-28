package com.inhabas.api.domain.member;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchoolInformation {

    @Enumerated(EnumType.STRING)
    private Major major;

    private Integer grade;

    private Integer gen;
}
