package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.inhabas.api.domain.lecture.domain.QStudent.student;
import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable) {

        List<StudentListDto> students = queryFactory.select(Projections.constructor(StudentListDto.class,
                        member.name.value,
                        member.studentId.id,
                        member.phone.value,
                        member.email.value,
                        Expressions.asNumber(0),
                        Expressions.asNumber(0),
                        student.status,
                        student.id
                ))
                .from(student)
                .innerJoin(member).on(member.studentId.eq(student.studentId))
                .where(student.lecture.id.eq(lectureId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(student.studentId.id.asc())
                .fetch();

        return new PageImpl<>(students, pageable, getCount(lectureId));
    }

    private Integer getCount(Integer lectureId) {
        return queryFactory.select(student.id)
                .from(student)
                .where(student.lecture.id.eq(lectureId))
                .fetch()
                .size();
    }
}
