package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;


@Repository
@Transactional
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberAuthorityProvider.RoleDto fetchRoleByStudentId(Long id) {
        Role role = queryFactory
                .select(member.ibasInformation.role).from(member)
                .where(member.id.eq(id))
                .fetchOne();

        return new MemberAuthorityProvider.RoleDto(role);
    }

    @Override
    public boolean isDuplicated(MemberDuplicationQueryCondition condition) {

        condition.verifyTwoParameters();

        return !queryFactory.selectFrom(member)
                .where(eqAny(condition))
                .limit(1).fetch().isEmpty();
    }

    @Override
    public List<Member> findAllByRoleAndStudentIdLike(Role role, String studentId) {

        return queryFactory.
                selectFrom(member)
                .where(eqRole(role)
                        .and(member.studentId.id.like("%" + studentId + "%")))
                .fetch();

    }

    @Override
    public List<Member> findAllByRoleAndNameLike(Role role, String name) {

        return queryFactory.
                selectFrom(member)
                .where(eqRole(role)
                        .and(member.name.value.like("%" + name + "%")))
                .fetch();

    }

    @Override
    public List<Member> findAllByRolesInAndStudentIdLike(Collection<Role> roles, String studentId) {

        return queryFactory.
                selectFrom(member)
                .where(member.ibasInformation.role.in(roles)
                    .and(member.studentId.id.like("%" + studentId + "%")))
                .fetch();

    }

    @Override
    public List<Member> findAllByRolesInAndNameLike(Collection<Role> roles, String name) {

        return queryFactory.
                selectFrom(member)
                .where(member.ibasInformation.role.in(roles)
                        .and(member.name.value.like("%" + name + "%")))
                .fetch();

    }


    private BooleanExpression eqRole(Role role) {
        return member.ibasInformation.role.eq(role);
    }

    private BooleanBuilder eqAny(MemberDuplicationQueryCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        return booleanBuilder.or(eqProvider(condition.getProvider()))
                .or(eqUid(condition.getUid()));
    }

    private BooleanExpression eqUid(UID uid) {
        return Objects.isNull(uid) ? null : member.uid.eq(uid);
    }

    private BooleanExpression eqProvider(OAuth2Provider provider) {
        return Objects.isNull(provider) ? null : member.provider.eq(provider);
    }
}
