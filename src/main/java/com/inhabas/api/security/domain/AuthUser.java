package com.inhabas.api.security.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_user",
        uniqueConstraints = { @UniqueConstraint(name = "unique_provider_and_email", columnNames = { "provider", "email" })})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String provider;

    private String email;

    @Column(name = "profile_id")
    private Integer profileId;

    private boolean hasJoined;

    private boolean isActive;

    private LocalDateTime lastLogin;

    public AuthUser(String provider, String email) {
        this.provider = provider;
        this.email = email;
        this.hasJoined = false;
        this.isActive = true;
        this.profileId = null;
        lastLogin = LocalDateTime.now();
    }

    public AuthUser setLastLoginTime(LocalDateTime time) {
        this.lastLogin = time;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public String getEmail() {
        return email;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean hasJoined() {
        return this.hasJoined;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public void setJoinFlag() {
        this.hasJoined = true;
    }
}
