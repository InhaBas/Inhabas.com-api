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
    @JoinColumn(name = "USER_MAJOR")
    private Major major;

    @Column(name = "USER_GRADE")
    private Integer grade;

    @Column(name = "USER_GEN")
    private Integer gen;
}
