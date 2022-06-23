package com.inhabas.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.MemberId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.beans.ConstructorProperties;

@Setter @Getter
public class CommentSaveDto {

    @JsonUnwrapped
    @NotNull @Positive
    private MemberId writerId;

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String contents;

    @NotNull @Positive
    private Integer boardId;

    @ConstructorProperties({"writerId", "contents", "boardId"})
    public CommentSaveDto(MemberId writerId, String contents, Integer boardId) {
        this.writerId = writerId;
        this.contents = contents;
        this.boardId = boardId;
    }
}
