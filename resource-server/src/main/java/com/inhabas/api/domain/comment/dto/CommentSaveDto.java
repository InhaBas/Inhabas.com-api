package com.inhabas.api.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveDto {

    @JsonUnwrapped
    @NotNull
    private MemberId writerId;

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String contents;

    @NotNull @Positive
    private Integer boardId;

    public CommentSaveDto(Integer writerId, String contents, Integer boardId) {
        this.writerId = new MemberId(writerId);
        this.contents = contents;
        this.boardId = boardId;
    }
}
