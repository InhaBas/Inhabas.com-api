package com.inhabas.api.dto.board;

<<<<<<< HEAD
=======
import com.inhabas.api.domain.board.Category;
>>>>>>> 3143015 ([feature] BoardDto 생성)
import com.inhabas.api.domain.board.NormalBoard;
import lombok.Getter;
import lombok.Setter;

<<<<<<< HEAD
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

=======
<<<<<<< HEAD
=======
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

>>>>>>> d32e304 (BoardDto 생성)
>>>>>>> 3143015 ([feature] BoardDto 생성)
@Getter @Setter
public class UpdateBoardDto {
    @NotNull
    private Integer id;
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

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotEmpty(message = "본문을 입력하세요")
    private String contents;

    @NotNull(message = "카테고리를 선택하세요.")
    private Integer categoryId;
>>>>>>> d32e304 (BoardDto 생성)

    public NormalBoard toEntity() {
        return null;
    }
}
