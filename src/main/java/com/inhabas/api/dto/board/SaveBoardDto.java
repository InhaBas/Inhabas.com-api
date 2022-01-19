package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.NormalBoard;
<<<<<<< HEAD

=======
>>>>>>> 3143015 ([feature] BoardDto 생성)
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
import javax.validation.constraints.*;
=======
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
>>>>>>> 3143015 ([feature] BoardDto 생성)

@Getter @Setter
@NoArgsConstructor
public class SaveBoardDto {
<<<<<<< HEAD
=======

>>>>>>> 3143015 ([feature] BoardDto 생성)
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;
<<<<<<< HEAD
=======

    private Category category;
>>>>>>> 3143015 ([feature] BoardDto 생성)

    public SaveBoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
