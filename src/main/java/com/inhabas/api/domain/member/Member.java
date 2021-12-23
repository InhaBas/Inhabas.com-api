package com.inhabas.api.domain.member;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER")
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "USER_STU")
    private Integer id;

    @Column(name = "USER_NAME")
    private String name;

    @Column(name = "USER_PHONE")
    private String phone;

    @Column(name = "USER_PIC")
    private String user_pic;

    @Embedded
    private SchoolInformation schoolInformation;

    @Embedded
    private IbasInformation ibasInformation;
}

@Embeddable @Getter
@AllArgsConstructor @NoArgsConstructor
class SchoolInformation {
    @ManyToOne
    @JoinColumn(name = "USER_MAJOR")
    private Major major;

    @Column(name = "USER_GRADE")
    private Integer grade;

    @Column(name = "USER_GEN")
    private Integer gen;
}

@Embeddable @Getter
@AllArgsConstructor @NoArgsConstructor
class IbasInformation {
    @Column(name = "USER_ROLE")
    private Role role;

    @Column(name = "USER_JOINED")
    private Date joined;

    @Column(name = "USER_INTRO")
    private String introduce;

    @Column(name = "USER_APPLY_PUBLISH")
    private Integer applyPublish;
}
