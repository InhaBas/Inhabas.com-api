package com.inhabas.api.auth.domain.oauth2.socialAccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private OAuth2Provider provider;

    @Embedded
    private UID uid;

    private LocalDateTime lastLogin;

    // 최초 로그인 날짜
    private LocalDateTime dateJoined;

    @Lob
    private String extraData;

    @Column(length = 1000)
    private String profileImageUrl;

    public SocialAccount(OAuth2Provider provider, String uid, LocalDateTime lastLogin, LocalDateTime dateJoined, String extraData) {
        this.provider = provider;
        this.uid = new UID(uid);
        this.lastLogin = lastLogin;
        this.dateJoined = dateJoined;
        this.extraData = extraData;
    }

    public SocialAccount(OAuth2UserInfo userInfo) {
        this.provider = userInfo.getProvider();
        this.uid =  new UID(userInfo.getId());
        this.lastLogin = LocalDateTime.now();
        this.dateJoined = LocalDateTime.now();
        this.profileImageUrl = userInfo.getImageUrl();
        try {
            this.extraData = new ObjectMapper().writeValueAsString(userInfo.getExtraData());
        } catch (JsonProcessingException ignored) {}
    }


    public String getUid() {
        return uid.getValue();
    }

    public OAuth2Provider getOAuth2Provider() {
        return provider;
    }

    public SocialAccount setLastLoginTime(LocalDateTime time) {
        this.lastLogin = time;
        return this;
    }
}
