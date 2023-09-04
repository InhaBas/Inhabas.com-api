package com.inhabas.api.domain.member.domain.entity;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.IbasInformation;
import com.inhabas.api.domain.member.domain.valueObject.MemberType;
import com.inhabas.api.domain.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.domain.member.domain.valueObject.Email;
import com.inhabas.api.domain.member.domain.valueObject.Name;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_PHONE", columnNames = "PHONE")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @EmbeddedId
    private MemberId id;

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

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Member(MemberId id, String name, String phone, String email, String picture, SchoolInformation schoolInformation, IbasInformation ibasInformation) {
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

    public Role getRole() {
        return this.ibasInformation.getRole();
    }


    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setRole(Role role) {
        this.ibasInformation.setRole(role);
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

    public boolean isSameMember(MemberId id) {
        return this.id.equals(id);
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


