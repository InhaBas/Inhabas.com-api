package com.inhabas.api.dto;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.file.BoardFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NormalBoardDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 100)
    private String title;

    @NotNull(message = "본문을 입력해주세요.")
    private String contents;

    @Nullable
    private Set<BoardFile> files = new HashSet<>();

    private String categoryName;

    // Entity to Dto
    public NormalBoardDto(NormalBoard normalBoard){
        this.title = normalBoard.getTitle();
        this.contents = normalBoard.getContents();
        Category entityCategory = normalBoard.getCategory();
        this.categoryName = entityCategory.name();
    }

    // Dto to Entity
    public NormalBoard toEntity() {
        return new NormalBoard(title, contents, files, categoryName);
    }
}


