package com.inhabas.api.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateDto {

    @NotNull
    private Integer id;

    @JsonUnwrapped
    @NotNull
    private MemberId writerId;

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String contents;

    @NonNull @Positive
    private Integer boardId;

    public CommentUpdateDto(Integer id, MemberId writerId, String contents, @NonNull Integer boardId) {
        this.id = id;
        this.writerId = writerId;
        this.contents = contents;
        this.boardId = boardId;
    }
}
