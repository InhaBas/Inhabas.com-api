package com.inhabas.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.MemberId;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.beans.ConstructorProperties;

@Setter @Getter
public class CommentUpdateDto {

    @NotNull
    private Integer id;

    @JsonUnwrapped
    @NotNull @Positive
    private MemberId writerId;

    @NotBlank @Length(max = 499, message = "500자 이하여야 합니다.")
    private String contents;

    @NonNull @Positive
    private Integer boardId;

    @ConstructorProperties({"id", "writerId", "contents", "boardId"})
    public CommentUpdateDto(Integer id, MemberId writerId, String contents, @NonNull Integer boardId) {
        this.id = id;
        this.writerId = writerId;
        this.contents = contents;
        this.boardId = boardId;
    }
}
