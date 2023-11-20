package com.inhabas.api.auth.domain.oauth2.member.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.*;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_PROVIDER_UID", columnNames = {"provider", "uid"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StudentId studentId;

    @Embedded
    private Name name;

    @Embedded
    private Phone phone;

    @Embedded
    private Email email;

    @Column(name = "picture", length = 1000)
    private String picture;

    @Embedded
    private SchoolInformation schoolInformation;

    @Embedded
    private IbasInformation ibasInformation;

    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    @Embedded
    private UID uid;

    @Column(name = "last_login", nullable = false)
    private LocalDateTime lastLogin;

    @Lob
    @Column(name = "extra_data", nullable = false)
    private String extraData;

    @Column(name = "is_deltee")
    private boolean isDeleted = false;

    @Builder
    public Member(StudentId studentId, String name, String phone, String email, String picture, SchoolInformation schoolInformation, IbasInformation ibasInformation) {
        this.studentId = studentId;
        this.name = new Name(name);
        this.phone = new Phone(phone);
        this.email = new Email(email);
        this.picture = picture;
        this.schoolInformation = schoolInformation;
        this.ibasInformation = ibasInformation;
    }

    @Builder
    public Member(OAuth2UserInfo userInfo) {
        this.provider = userInfo.getProvider();
        this.uid =  new UID(userInfo.getId());
        this.lastLogin = LocalDateTime.now();
        this.picture = userInfo.getImageUrl();
        this.email = new Email(userInfo.getEmail());
        this.ibasInformation = new IbasInformation(ANONYMOUS);
        try {
            this.extraData = new ObjectMapper().writeValueAsString(userInfo.getExtraData());
        } catch (JsonProcessingException ignored) {}
    }

    @Builder
    public Member(String name, String email, Role role) {
        this.name = new Name(name);
        this.email = new Email(email);
        this.ibasInformation = new IbasInformation(role);
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

    public void setName(String name) {
        this.name = new Name(name);
    }

    public void setEmail(String email) {
        this.email = new Email(email);
    }

    public void setRole(Role role) {
        this.ibasInformation.setRole(role);
    }

    public Member setLastLoginTime(LocalDateTime time) {
        this.lastLogin = time;
        return this;
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

    public boolean isSameMember(StudentId id) {
        return this.studentId.equals(id);
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


