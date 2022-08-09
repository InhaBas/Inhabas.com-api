package com.inhabas.api.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveDto {

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String contents;

    @NotNull @Positive
    private Integer boardId;

    @Positive
    private Integer parentCommentId;

    public CommentSaveDto(String contents, Integer boardId) {
        this.contents = contents;
        this.boardId = boardId;
    }

    public CommentSaveDto(String contents, Integer boardId, Integer parentCommentId) {
        this.contents = contents;
        this.boardId = boardId;
        this.parentCommentId = parentCommentId;
    }

    @JsonIgnore
    public boolean isNotRootComment() {
        return this.parentCommentId != null;
    }
}
