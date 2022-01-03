package com.inhabas.api.domain.member;

import com.inhabas.api.domain.comment.Comment;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(Member.class.isAssignableFrom(o.getClass()))) return false;
        Member member = (Member) o;
        return getId().equals(member.getId())
                && getName().equals(member.getName())
                && getPhone().equals(member.getPhone())
                && getPicture().equals(member.getPicture())
                && getSchoolInformation().equals(member.getSchoolInformation())
                && getIbasInformation().equals(member.getIbasInformation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getName(),
                getPhone(),
                getPicture(),
                getSchoolInformation(),
                getIbasInformation());
    }
}


