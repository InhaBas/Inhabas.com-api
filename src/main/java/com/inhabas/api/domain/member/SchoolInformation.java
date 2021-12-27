package com.inhabas.api.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolInformation {
    @ManyToOne
    private Major major;

    private Integer grade;

    private Integer gen;
}
