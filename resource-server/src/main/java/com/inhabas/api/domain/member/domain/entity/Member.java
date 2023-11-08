package com.inhabas.api.domain.member.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.domain.member.domain.valueObject.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_PHONE", columnNames = "PHONE")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberId memberId;

    @Embedded
    private Name name;

    @Embedded
    private Phone phone;

    @Embedded
    private Email email;

    @Column(name = "PICTURE", length = 1000)
    private String picture;

    @Embedded
    private SchoolInformation schoolInformation;

    @Embedded
    private IbasInformation ibasInformation;

    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    @Embedded
    private UID uid;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @Lob
    private String extraData;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Member(MemberId memberId, String name, String phone, String email, String picture, SchoolInformation schoolInformation, IbasInformation ibasInformation) {
        this.memberId = memberId;
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

    public boolean isSameMember(MemberId id) {
        return this.memberId.equals(id);
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


