package com.inhabas.api.domain.board;

import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.file.BoardFile;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
public class BaseBoardDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 100)
    private String title;

    @NotNull(message = "본문을 입력해주세요.")
    private String contents;

    private Set<BoardFile> files = new HashSet<>();

    /*
    public BaseBoard toEntity(){
        Title entityTitle = new Title(title);
        Contents entityContents = new Contents(contents);
        Set<BoardFile> entityFiles = files;
        return new BaseBoard(entityTitle, entityContents, entityFiles);
    }*/

    public BaseBoard toEntity(){
        Title entityTitle = new Title(title);
        Contents entityContents = new Contents(contents);
        Set<BoardFile> entityFiles = files;
        return BaseBoard.builder()
                .title(entityTitle)
                .contents(entityContents)
                .files(entityFiles)
                .build();
    }
}
