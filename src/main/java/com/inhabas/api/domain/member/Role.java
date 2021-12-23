package com.inhabas.api.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

public enum Role {
    chief,
    subChief,
    operation,
    accountant,
    professor,
    IT,
    normalMember,
    inactiveMember,
    notApproved,
    anonymous
}


//@Entity
//@Table(name = "USER_ROLE")
//@Getter @NoArgsConstructor
//public class Role {
//    @Id @Column(name = "ROLE_NO")
//    private Integer id;
//
//    @Column(name = "ROLE_NAME")
//    private String name;
//
//    @OneToMany(mappedBy = "ibasInformation.role")
//    private List<Member> members;
//}
