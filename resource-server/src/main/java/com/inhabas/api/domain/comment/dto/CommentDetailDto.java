package com.inhabas.api.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentDetailDto {

    private Integer commentId;
    private String contents;
    @JsonUnwrapped(prefix = "writer_")
    private StudentId writerId;
    private String writerName;
    private String writerMajor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime created;
    private List<CommentDetailDto> children;

    public static CommentDetailDto fromEntity(Comment comment) {
        return new CommentDetailDto(
                comment.getId(),
                comment.getContents(),
                comment.getWriter().getStudentId(),
                comment.getWriter().getName(),
                comment.getWriter().getSchoolInformation().getMajor(),
                comment.getDateCreated()
        );
    }

    public CommentDetailDto(Integer id, String contents, StudentId writerId, String memberName, String major, LocalDateTime created) {
        this.commentId = id;
        this.contents = contents;
        this.writerId = writerId;
        this.writerName = memberName;
        this.writerMajor = major;
        this.created = created;
        this.children = new ArrayList<>();
    }


}
