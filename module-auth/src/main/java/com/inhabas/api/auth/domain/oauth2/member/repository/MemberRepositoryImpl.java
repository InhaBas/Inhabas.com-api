package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Phone;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;


@Repository
@Transactional
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberAuthorityProvider.RoleDto fetchRoleByMemberId(StudentId studentId) {
        Role role = queryFactory
                .select(member.ibasInformation.role).from(member)
                .where(member.studentId.eq(studentId))
                .fetchOne();

        return new MemberAuthorityProvider.RoleDto(role);
    }

    @Override
    public boolean isDuplicated(MemberDuplicationQueryCondition condition) {

        condition.verifyAtLeastOneParameter();

        return !queryFactory.selectFrom(member)
                .where(eqAny(condition))
                .limit(1).fetch().isEmpty();
    }

    @Override
    public List<Member> findByRoleAndIdLike(Role role, StudentId studentId) {
        final List<Member> members = queryFactory.
                selectFrom(member)
                .where(eqRole(role)
                        .and(member.studentId.id.like("%" + studentId.toString() + "%")))
                .fetch();

        return members;
    }

    @Override
    public List<Member> findByRoleAndNameLike(Role role, Name name) {
        List<Member> members = queryFactory.
                selectFrom(member)
                .where(eqRole(role)
                        .and(member.name.value.like("%" + name.getValue() + "%")))
                .fetch();

        return members;
    }

    @Override
    public List<Member> findByIdLike(StudentId studentId) {
        List<Member> members = queryFactory.
                selectFrom(member)
                .where((member.studentId.id.like("%" + studentId.toString() + "%")))
                .fetch();

        return members;
    }

    private BooleanExpression eqRole(Role role) {
        return member.ibasInformation.role.eq(role);
    }

    private BooleanBuilder eqAny(MemberDuplicationQueryCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        return booleanBuilder.or(eqId(condition.getStudentId()))
                .or(eqPhone(condition.getPhone()));
    }

    private BooleanExpression eqId(StudentId studentId) {
        return Objects.isNull(studentId) ? null : member.studentId.eq(studentId);
    }

    private BooleanExpression eqPhone(Phone phoneNumber) {
        return Objects.isNull(phoneNumber) ? null : member.phone.eq(phoneNumber);
    }
}
