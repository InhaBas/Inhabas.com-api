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


