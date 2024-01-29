package com.inhabas.api.domain.lecture.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.lecture.domain.converter.StudentStatusConverter;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Table(
    name = "lecture_students",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "lecture_students_unique_index",
          columnNames = {"lecture_id", "user_id"})
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Lecture lecture;

  @Embedded
  @AttributeOverride(
      name = "id",
      column = @Column(name = "user_id", updatable = false, nullable = false))
  private StudentId studentId;

  @Convert(converter = StudentStatusConverter.class)
  @Column(columnDefinition = "TINYINT(4)")
  private StudentStatus status;

  public Student(Lecture lecture, StudentId studentId) {

    if (lecture.isHeldBy(studentId))
      throw new IllegalArgumentException("강의자는 자신의 강의에 수강생으로 등록될 수 없습니다.");

    this.lecture = lecture;
    this.studentId = studentId;
    this.status = StudentStatus.PROGRESS;
  }

  public Student changeStatusByLecturer(StudentStatus status, StudentId lecturerId) {

    if (!lecture.isHeldBy(lecturerId)) throw new AccessDeniedException("강의자만 수강생 정보를 변경할 수 있습니다");

    if (status != StudentStatus.BLOCKED && status != StudentStatus.PROGRESS)
      throw new IllegalArgumentException("해당 상태로는 변경할 수 없습니다!");

    this.status = status;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void exitLecture() {

    this.status = StudentStatus.EXIT;
  }
}
