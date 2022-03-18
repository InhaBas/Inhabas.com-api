package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.service.signup.NoQueryParameterException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.inhabas.api.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public boolean isDuplicated(MemberDuplicationQueryCondition condition) {

        condition.verityAtLeastOneParameter();

        return !queryFactory.selectFrom(QMember.member)
                .where(eqAny(condition))
                .limit(1).fetch().isEmpty();
    }

    private BooleanBuilder eqAny(MemberDuplicationQueryCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        return booleanBuilder.or(eqMemberId(condition.getMemberId()))
                .or(eqPhone(condition.getPhone()));
    }

    private BooleanExpression eqMemberId(Integer id) {
        return Objects.isNull(id) ? null : member.id.eq(id);
    }

    private BooleanExpression eqPhone(Phone phoneNumber) {
        return Objects.isNull(phoneNumber) ? null : member.phone.eq(phoneNumber);
    }
}
