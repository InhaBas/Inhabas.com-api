package com.inhabas.api.domain.member.domain.entity;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.IbasInformation;
import com.inhabas.api.domain.member.domain.valueObject.MemberType;
import com.inhabas.api.domain.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.domain.member.domain.valueObject.Email;
import com.inhabas.api.domain.member.domain.valueObject.Name;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.team.domain.MemberTeam;
import com.inhabas.api.domain.team.domain.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

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
    private Phone phoneNumber;

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
    public Member(MemberId id, String name, String phoneNumber, String email, String picture, SchoolInformation schoolInformation, IbasInformation ibasInformation) {
        this.id = id;
        this.name = new Name(name);
        this.phoneNumber = new Phone(phoneNumber);
        this.email = new Email(email);
        this.picture = picture;
        this.schoolInformation = schoolInformation;
        this.ibasInformation = ibasInformation;
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getPhone() {
        return this.phoneNumber.getValue();
    }

    public String getEmail() {
        return this.email.getValue();
    }

    public Role getRole() {
        return this.ibasInformation.getRole();
    }

    /**
     * N+1 쿼리 유의하면서 사용할 것.
     * @return {@code UnmodifiableList}
     */
    public Collection<Team> getTeamList() {
        return this.ibasInformation.getTeamList().stream()
                .map(MemberTeam::getTeam)
                .collect(Collectors.toUnmodifiableList());
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


