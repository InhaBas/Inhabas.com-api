package com.inhabas.api.domain.member.security.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.domain.member.MemberId;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.inhabas.api.domain.member.security.socialAccount.QMemberSocialAccount.memberSocialAccount;

@RequiredArgsConstructor
public class MemberSocialAccountRepositoryImpl implements MemberSocialAccountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<MemberId> findMemberIdByUidAndProvider(UID uid, OAuth2Provider provider) {
        return Optional.ofNullable(jpaQueryFactory
                .select(memberSocialAccount.member.id)
                .where(eqSocialAccount(uid, provider))
                .from(memberSocialAccount)
                .fetchOne());
    }

    private BooleanExpression eqSocialAccount(UID uid, OAuth2Provider provider) {
        return memberSocialAccount.uid.eq(uid).and(memberSocialAccount.provider.eq(provider));
    }
}
