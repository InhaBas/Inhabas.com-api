package com.inhabas.api.dto.board;

<<<<<<< HEAD
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
=======
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
>>>>>>> e8699437bcf1a359625e0ee6b7f91bdf4a683f2a

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
<<<<<<< HEAD
=======
@NoArgsConstructor(access = AccessLevel.PROTECTED)
>>>>>>> e8699437bcf1a359625e0ee6b7f91bdf4a683f2a
public class SaveBoardDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Length(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    @NotNull
    private Integer menuId;

    @NotNull
    private Integer loginedUser;

    public SaveBoardDto(String title, String contents, Integer menuId, Integer loginedUser) {
        this.title = title;
        this.contents = contents;
        this.menuId = menuId;
        this.loginedUser = loginedUser;
    }
}
