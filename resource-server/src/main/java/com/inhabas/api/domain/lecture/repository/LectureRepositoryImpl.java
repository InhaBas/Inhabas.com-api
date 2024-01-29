package com.inhabas.api.domain.lecture.repository;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;
import static com.inhabas.api.domain.lecture.domain.QLecture.lecture;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember;
import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private final QMember member2 = new QMember("member2");

  public Optional<LectureDetailDto> getDetails(Integer id) {

    return Optional.ofNullable(
        queryFactory
            .select(
                Projections.constructor(
                    LectureDetailDto.class,
                    lecture.id,
                    lecture.title,
                    Projections.constructor(
                        LectureDetailDto.MemberInfo.class,
                        lecture.chief.id,
                        member.schoolInformation.major.value,
                        member.name.value),
                    lecture.applyDeadline,
                    lecture.daysOfWeek,
                    lecture.place,
                    lecture.introduction,
                    lecture.curriculumDetails,
                    lecture.participantsLimits,
                    lecture.method,
                    lecture.status,
                    lecture.rejectReason,
                    lecture.paid,
                    lecture.dateCreated,
                    lecture.dateUpdated))
            .from(lecture)
            .innerJoin(member)
            .on(member.studentId.eq(lecture.chief))
            .where(lecture.id.eq(id))
            .fetchOne());
  }

  public Page<LectureListDto> getList(Pageable pageable) {

    List<LectureListDto> list =
        queryFactory
            .select(
                Projections.constructor(
                    LectureListDto.class,
                    lecture.id,
                    lecture.title,
                    lecture.chief.id,
                    lecture.introduction,
                    lecture.applyDeadline,
                    lecture.status,
                    lecture.method,
                    lecture.participantsLimits,
                    Expressions.asNumber(0)))
            .from(lecture)
            .where(lecture.status.in(LectureStatus.PROGRESSING, LectureStatus.TERMINATED))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(lecture.dateCreated.desc())
            .orderBy(lecture.status.asc())
            .fetch();

    return new PageImpl<>(list, pageable, getCount());
  }

  private Integer getCount() {

    return queryFactory.selectFrom(lecture).fetch().size();
  }
}
