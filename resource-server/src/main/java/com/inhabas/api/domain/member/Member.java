package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.MemberType;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Email;
import com.inhabas.api.domain.member.type.wrapper.Name;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "unique_phone", columnNames = "phone")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    private Integer id;

    @Embedded
    private Name name;

    @Embedded
    private Phone phone;

    @Embedded
    private Email email;

    @Column(name = "picture", length = 500)
    private String picture;

    @Embedded
    private SchoolInformation schoolInformation;

    @Embedded
    private IbasInformation ibasInformation;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Member(Integer id, String name, String phone, String email, String picture, SchoolInformation schoolInformation, IbasInformation ibasInformation) {
        this.id = id;
        this.name = new Name(name);
        this.phone = new Phone(phone);
        this.email = new Email(email);
        this.picture = picture;
        this.schoolInformation = schoolInformation;
        this.ibasInformation = ibasInformation;
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getPhone() {
        return this.phone.getValue();
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setRole(Role role) {
        this.ibasInformation.setRole(role);
    }

    public void addTeam(MemberTeam team) {
        this.ibasInformation.addTeam(team);
    }

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

    public boolean isSameMember(Integer id) {
        return Objects.equals(this.id, id);
    }

    public boolean isUnderGraduate() {
        return this.schoolInformation.getMemberType() == MemberType.UNDERGRADUATE;
    }
    public boolean isGraduated() {
        return this.schoolInformation.getMemberType() == MemberType.GRADUATED;
    }
    public boolean isProfessor() {
        return this.schoolInformation.getMemberType() == MemberType.PROFESSOR;
    }
    public boolean isOther() {
        return this.schoolInformation.getMemberType() == MemberType.OTHER;
    }
    public boolean isBachelor() {
        return this.schoolInformation.getMemberType() == MemberType.BACHELOR;
    }

    public boolean isCompleteToSignUp() {
        return this.ibasInformation.isCompleteToSignUp();
    }

    public void finishSignUp() {
        this.ibasInformation.finishSignUp();
    }
}


