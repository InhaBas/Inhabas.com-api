package com.inhabas.api.security.domain;

import com.inhabas.api.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member profile;

    private boolean hasJoined;

    private boolean isActive;

    private LocalDateTime lastLogin;

    public AuthUser(String provider, String email) {
        this.provider = provider;
        this.email = email;
        this.hasJoined = false;
        this.isActive = true;
        this.profile = null;
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

    public Member getProfile() {
        return profile;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean hasJoined() {
        return this.hasJoined;
    }
}
