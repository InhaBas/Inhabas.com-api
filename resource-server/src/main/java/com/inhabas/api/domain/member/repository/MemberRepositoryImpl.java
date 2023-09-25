package com.inhabas.api.domain.member.repository;

import static com.inhabas.api.domain.member.domain.entity.QMember.member;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Name;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.domain.member.dto.NewMemberManagementDto;
import com.inhabas.api.domain.member.security.MemberAuthorityProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public List<Member> findByRoleAndIdLike(Role role, MemberId memberId) {
        final List<Member> members = queryFactory.
                selectFrom(member)
                .where(eqRole(role)
                        .and(member.id.id.like("%" + memberId.toString() + "%")))
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

        return new PageImpl<>(members, pageable, total);
    }

    @Override
    public List<Member> findByIdLike(MemberId memberId) {
        List<Member> members = queryFactory.
                selectFrom(member)
                .where((member.id.id.like("%" + memberId.toString() + "%")))
                .fetch();

        return members;
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
