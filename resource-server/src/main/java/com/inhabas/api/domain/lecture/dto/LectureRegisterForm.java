package com.inhabas.api.domain.lecture.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureRegisterForm {
    @NotBlank
    @Length(max = 100)
    private String title;

    @NotNull @Future
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applyDeadLine;

    @NotBlank @Length(max = 20)
    private String daysOfWeeks;

    @NotNull @Length(max = 300)
    private String place;

    @NotNull @Length(max = 300)
    private String introduction;

    @NotNull @Length(max = 16777215)
    private String curriculumDetails;

    @NotNull
    private Integer participantsLimits;

    @NotNull
    private Integer method;

    public LectureRegisterForm(String title, LocalDateTime applyDeadLine, String daysOfWeeks, String place, String introduction, String curriculumDetails, Integer participantsLimits, Integer method) {
        this.title = title;
        this.applyDeadLine = applyDeadLine;
        this.daysOfWeeks = daysOfWeeks;
        this.place = place;
        this.introduction = introduction;
        this.curriculumDetails = curriculumDetails;
        this.participantsLimits = participantsLimits;
        this.method = method;
    }

    public Lecture toEntity(StudentId studentId) {
        return Lecture.builder()
                .applyDeadline(applyDeadLine)
                .chief(studentId)
                .daysOfWeek(daysOfWeeks)
                .curriculumDetails(curriculumDetails)
                .introduction(introduction)
                .method(method)
                .title(title)
                .participantsLimits(participantsLimits)
                .place(place)
                .build();
    }
}
