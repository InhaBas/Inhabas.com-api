package com.inhabas.api.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IbasInformation {
    @Column(name = "USER_ROLE")
    private Role role;

    @Column(name = "USER_JOINED")
    private Date joined;

    @Column(name = "USER_INTRO")
    private String introduce;

    @Column(name = "USER_APPLY_PUBLISH")
    private Integer applyPublish;
}