package com.inhabas.api.domain.member;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    private Integer id;

    private String name;

    private String phone;

    private String picture;

    @Embedded
    private SchoolInformation schoolInformation;

    @Embedded
    private IbasInformation ibasInformation;
}


