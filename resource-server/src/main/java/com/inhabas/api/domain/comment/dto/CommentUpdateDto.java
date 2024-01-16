package com.inhabas.api.domain.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateDto {

    @NotNull
    private Long commentId;

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String content;

    public CommentUpdateDto(Long id, String content) {
        this.commentId = id;
        this.content = content;
    }
}
