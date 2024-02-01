package com.inhabas.api.domain.signUp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ApplicationDetailDto {

    @NotNull
    @Positive
    private Long memberId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer grade;

    @NotBlank
    private String major;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
    private String phoneNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type="string" , example = "2024-11-01T00:00:00")
    private LocalDateTime dateJoined;

    @NotNull
    private List<QuestionAnswerDto> answers;

    @Builder
    public ApplicationDetailDto(Long memberId, String name, Integer grade, String major, String email,
                                String phoneNumber, LocalDateTime dateJoined, List<QuestionAnswerDto> answers) {
        this.memberId = memberId;
        this.name = name;
        this.grade = grade;
        this.major = major;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.answers = answers;
    }

}
