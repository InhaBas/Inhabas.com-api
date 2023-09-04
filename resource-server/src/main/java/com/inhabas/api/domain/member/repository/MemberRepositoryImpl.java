package com.inhabas.api.domain.member.repository;

import static com.inhabas.api.domain.member.domain.entity.QMember.member;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.domain.member.security.MemberAuthorityProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberAuthorityProvider.RoleDto fetchRoleByMemberId(MemberId memberId) {
        Role role = queryFactory
                .select(member.ibasInformation.role).from(member)
                .where(member.id.eq(memberId))
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
    public List<Member> searchAllByRole(Role role) {

        return queryFactory.select(member)
                .from(member)
                .where(eqRole(role))
                .fetch();
    }

    @Override
    public List<Member> searchByRoleLimit(Role role, Integer limit) {

        return queryFactory.select(member)
                .from(member)
                .where(eqRole(role))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression eqRole(Role role) {
        return member.ibasInformation.role.eq(role);
    }

    private BooleanBuilder eqAny(MemberDuplicationQueryCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        return booleanBuilder.or(eqId(condition.getMemberId()))
                .or(eqPhone(condition.getPhone()));
    }

    private BooleanExpression eqId(MemberId id) {
        return Objects.isNull(id) ? null : member.id.eq(id);
    }

    private BooleanExpression eqPhone(Phone phoneNumber) {
        return Objects.isNull(phoneNumber) ? null : member.phone.eq(phoneNumber);
    }
}
