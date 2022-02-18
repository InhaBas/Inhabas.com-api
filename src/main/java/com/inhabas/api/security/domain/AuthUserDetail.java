package com.inhabas.api.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This instance includes exactly the same data of AuthUser!
 * After finishing authentication, AuthUser's data is saved as AuthUserDetail into SecurityContext.
 * It's risky to use directly AuthUser because AuthUser is JPA Entity.
 * (whenever use JPA Entity, we should make sure to know what the Entity's status is in PersistenceContext at that time).
 *
 * we never intend to modify AuthUser Entity in normal business logic.
 * (such as "Controllers", "Services", etc.).
 *
 */
@Setter @Getter
public class AuthUserDetail {

    private Integer id;

    private String provider;

    private String email;

    private Integer profileId;

    private boolean hasJoined;

    private boolean isActive;

    private String profileImageUrl;

    @Builder
    public AuthUserDetail(Integer id, String provider, String email, Integer profileId, boolean hasJoined, boolean isActive) {
        this.id = id;
        this.provider = provider;
        this.email = email;
        this.profileId = profileId;
        this.hasJoined = hasJoined;
        this.isActive = isActive;
    }

    public static AuthUserDetail convert(AuthUser authUser) {
        return AuthUserDetail.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .provider(authUser.getProvider())
                .profileId(authUser.getProfileId())
                .hasJoined(authUser.hasJoined())
                .isActive(authUser.isActive())
                .build();
    }

    public boolean hasJoined() {
        return hasJoined;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
