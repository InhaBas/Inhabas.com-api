package com.inhabas.api.domain.member;

import com.inhabas.api.auth.domain.socialAccount.SocialAccount;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_socialaccount")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSocialAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "socialaccount_id", foreignKey = @ForeignKey(name = "fk_to_socialaccount"))
    private SocialAccount socialAccount;

    public MemberSocialAccount(Member member, SocialAccount socialaccount) {
        this.member = member;
        this.socialAccount = socialaccount;
    }

    public Integer getId() {
        return this.id;
    }
}
