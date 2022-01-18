package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardDto {
    private Integer id;
    @NotBlank
    private String title;
    @NotEmpty
    private String contents;
    @NotNull
    private Integer category_id;

    public NormalBoard toEntity() {
        return null;
    }
}
