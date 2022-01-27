package com.inhabas.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.comment.Comment;
import com.inhabas.api.domain.member.type.wrapper.Major;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CommentDetailDto {

    private Integer id;
    private String contents;
    private Integer writerId;
    private String writerName;
    private Major writerMajor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime created;
    private List<CommentDetailDto> children;

    public static CommentDetailDto fromEntity(Comment comment) {
        return new CommentDetailDto(
                comment.getId(),
                comment.getContents(),
                comment.getWriter().getId(),
                comment.getWriter().getName(),
                comment.getWriter().getSchoolInformation().getMajor(),
                comment.getCreated()
        );
    }

    public CommentDetailDto(Integer id, String contents, Integer memberId, String memberName, Major major, LocalDateTime created) {
        this.id = id;
        this.contents = contents;
        this.writerId = memberId;
        this.writerName = memberName;
        this.writerMajor = major;
        this.created = created;
        this.children = new ArrayList<>();
    }
}
