package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;


@Repository
@Transactional
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final OrderSpecifier<Integer> ORDER_BY_ROLE = new CaseBuilder()
            .when(member.ibasInformation.role.eq(ADMIN)).then(1)
            .when(member.ibasInformation.role.eq(CHIEF)).then(2)
            .when(member.ibasInformation.role.eq(VICE_CHIEF)).then(3)
            .when(member.ibasInformation.role.eq(EXECUTIVES)).then(4)
            .when(member.ibasInformation.role.eq(SECRETARY)).then(5)
            .when(member.ibasInformation.role.eq(BASIC)).then(6)
            .when(member.ibasInformation.role.eq(DEACTIVATED)).then(7)
            .otherwise(8).asc();


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
                        .and(member.name.value.like("%" + name + "%"))
                        .and(member.schoolInformation.memberType.ne(MemberType.GRADUATED)))
                .fetch();

    }

    @Override
    public List<Member> findAllByRolesInAndStudentIdLike(Collection<Role> roles, String studentId) {

        OrderSpecifier<String> orderByStudentId = member.studentId.id.asc();

        return queryFactory.
                selectFrom(member)
                .where(member.ibasInformation.role.in(roles)
                        .and(member.studentId.id.like("%" + studentId + "%"))
                        .and(member.schoolInformation.memberType.ne(MemberType.GRADUATED)))
                .orderBy(ORDER_BY_ROLE, orderByStudentId)
                .fetch();

    }

    @Override
    public List<Member> findAllByRolesInAndNameLike(Collection<Role> roles, String name) {

        OrderSpecifier<String> orderByStudentId = member.studentId.id.asc();

        return queryFactory.
                selectFrom(member)
                .where(member.ibasInformation.role.in(roles)
                        .and(member.name.value.like("%" + name + "%")))
                .orderBy(ORDER_BY_ROLE, orderByStudentId)
                .fetch();

    }

    @Override
    public List<Member> findAllGraduatedByRolesInAndStudentLike(String studentId) {

        OrderSpecifier<String> orderByStudentId = member.studentId.id.asc();

        return queryFactory
                .selectFrom(member)
                .where(member.schoolInformation.memberType.eq(MemberType.GRADUATED)
                        .and(member.studentId.id.like("%" + studentId + "%")))
                .orderBy(ORDER_BY_ROLE, orderByStudentId)
                .fetch();

    }

    @Override
    public List<Member> findAllGraduatedByRolesInAndNameLike(String name) {

        OrderSpecifier<String> orderByStudentId = member.studentId.id.asc();

        return queryFactory
                .selectFrom(member)
                .where(member.schoolInformation.memberType.eq(MemberType.GRADUATED)
                        .and(member.name.value.like("%" + name + "%")))
                .orderBy(ORDER_BY_ROLE, orderByStudentId)
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
