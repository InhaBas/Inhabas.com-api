package com.inhabas.api.dto.board;

<<<<<<< HEAD
=======
import com.inhabas.api.domain.board.Category;
>>>>>>> 3143015 ([feature] BoardDto 생성)
import com.inhabas.api.domain.board.NormalBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
<<<<<<< HEAD
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

=======
<<<<<<< HEAD
=======
=======
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

<<<<<<< HEAD
>>>>>>> d32e304 (BoardDto 생성)
>>>>>>> 3143015 ([feature] BoardDto 생성)
=======
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)
@Getter @Setter
@NoArgsConstructor
public class UpdateBoardDto {
    @NotNull
    private Integer id;
<<<<<<< HEAD
<<<<<<< HEAD

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
=======
<<<<<<< HEAD
>>>>>>> 3143015 ([feature] BoardDto 생성)
    private String title;

    @NotBlank(message = "본문을 입력하세요")
    private String contents;

    public UpdateBoardDto(Integer id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public NormalBoard toEntity() {
        return null;
    }
=======
=======
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotEmpty(message = "본문을 입력하세요")
    private String contents;

    @NotNull(message = "카테고리를 선택하세요.")
    private Integer categoryId;

    public NormalBoard toEntity() {
        return null;
    }
}
