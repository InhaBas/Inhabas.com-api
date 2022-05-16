package com.inhabas.api.domain.member;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.domain.member.type.wrapper.Email;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_socialaccount",
        uniqueConstraints = { @UniqueConstraint(name = "user_socialaccount_uid_provider_uindex", columnNames = {"provider", "uid"})}) //
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSocialAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    private Email email;

    @Embedded
    private UID uid;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;


    public MemberSocialAccount(Member member, String email, String uid, OAuth2Provider provider) {
        this.member = member;
        this.email = new Email(email);
        this.uid = new UID(uid);
        this.provider = provider;
    }

    public Integer getId() {
        return this.id;
    }

    public Member getMember() {
        return this.member;
    }
}
