package com.inhabas.api.domain.comment;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.BaseBoard;
import com.inhabas.api.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "comment")
@NoArgsConstructor @Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private Member writer;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_comment_to_baseboard"))
    private BaseBoard parentBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_ref", foreignKey = @ForeignKey(name = "fk_comment_to_comment"))
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> children = new ArrayList<>();

    // comment 과 baseBoard 의 연관관계 편의 메소드
    public void setParentBoard(BaseBoard newParentBoard) {
        // 기존의 comment-board 연관관계를 끊는다.
        if (Objects.nonNull(this.parentBoard)) {
            this.parentBoard.getComments().remove(this);
        }
        this.parentBoard = newParentBoard;
        parentBoard.addComment(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(Comment.class.isAssignableFrom(o.getClass()))) return false;
        Comment comment = (Comment) o;
        return getId().equals(comment.getId())
                && getWriter().equals(comment.getWriter())
                && getContents().equals(comment.getContents())
                && getParentBoard().equals(comment.getParentBoard())
                && Objects.equals(getParentComment(), comment.getParentComment())
                && Objects.equals(getChildren(), comment.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWriter(), getContents(), getParentBoard(), getParentComment(), getChildren());
    }
}
