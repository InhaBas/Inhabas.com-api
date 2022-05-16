package com.inhabas.api.domain.member;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.inhabas.api.domain.member.QMemberSocialAccount.memberSocialAccount;
import static com.inhabas.api.domain.member.QTeam.team;

@RequiredArgsConstructor
public class MemberSocialAccountRepositoryImpl implements MemberSocialAccountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<MemberSocialAccount> findByUidAndProviderWithRoleAndTeam(UID uid, OAuth2Provider provider) {

        return Optional.ofNullable(jpaQueryFactory
                .from(memberSocialAccount)
                .join(memberSocialAccount.member).fetchJoin()
                .leftJoin(QMemberTeam.memberTeam).on(memberEqMemberTeam()).fetchJoin()
                .leftJoin(team).on(memberTeamEqTeam()).fetchJoin()
                        .select(memberSocialAccount).fetchOne());
    }

    private BooleanExpression memberTeamEqTeam() {
        return QMemberTeam.memberTeam.team.id.eq(team.id);
    }

    private BooleanExpression memberEqMemberTeam() {
        return memberSocialAccount.member.eq(QMemberTeam.memberTeam.member);
    }
}
