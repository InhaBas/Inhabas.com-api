package com.inhabas.api.security.domain.socialAccount;

import com.inhabas.api.domain.member.MemberSocialAccount;
import com.inhabas.api.security.domain.socialAccount.type.Provider;
import com.inhabas.api.security.domain.socialAccount.type.UID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "socialaccount",
        uniqueConstraints = { @UniqueConstraint(name = "unique_socialaccount", columnNames = {"provider", "uid"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Embedded
    private UID uid;

    private LocalDateTime lastLogin;

    private LocalDateTime dateJoined;

    private String extraData;

    @Column(length = 1000)
    private String profileImageUrl;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "socialAccount", cascade = CascadeType.REMOVE)
    private MemberSocialAccount memberSocialAccount;

    public SocialAccount(Provider provider, UID uid, LocalDateTime lastLogin, LocalDateTime dateJoined, String extraData) {
        this.provider = provider;
        this.uid = uid;
        this.lastLogin = lastLogin;
        this.dateJoined = dateJoined;
        this.extraData = extraData;
    }

    public SocialAccount(OAuth2UserRequest request) {

    }

    public String getUid() {
        return uid.getValue();
    }

    public Provider getProvider() {
        return provider;
    }

    public SocialAccount setLastLoginTime(LocalDateTime time) {
        this.lastLogin = time;
        return this;
    }
    public void mappingTo(MemberSocialAccount member) {
        this.memberSocialAccount = member;
    }
}
