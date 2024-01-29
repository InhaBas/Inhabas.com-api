package com.inhabas.api.domain.comment.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.exception.WriterUnmodifiableException;
import com.inhabas.api.domain.comment.domain.valueObject.Content;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "COMMENT")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_COMMENT_OF_USER_ID"))
  private Member writer;

  @Embedded private Content content;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "FK_COMMENT_OF_BOARD_ID"))
  private BaseBoard parentBoard;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PARENT_COMMENT",
      foreignKey = @ForeignKey(name = "FK_COMMENT_OF_COMMENT_REF_ID"))
  private Comment parentComment;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Comment> childrenComment = new ArrayList<>();

  @Column(nullable = false)
  private Boolean isDeleted = false;

  /* constructor */

  public Comment(String content, Member writer, BaseBoard parentBoard) {
    this.content = new Content(content);
    this.writtenBy(writer);
    this.toBoard(parentBoard);
  }

  /* getter */

  public Long getId() {
    return id;
  }

  public Member getWriter() {
    return writer;
  }

  public BaseBoard getParentBoard() {
    return parentBoard;
  }

  public Comment getParentComment() {
    return parentComment;
  }

  public List<Comment> getChildrenComment() {
    return Collections.unmodifiableList(childrenComment);
  }

  public String getContent() {
    return this.content.getValue();
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public Long update(String content) {
    this.content = new Content(content);
    return this.id;
  }

  /* relation methods */

  public Comment toBoard(BaseBoard newBoard) {
    if (Objects.nonNull(this.parentBoard))
      throw new IllegalStateException("댓글을 다른 게시글로 옮길 수 없습니다.");

    this.parentBoard = newBoard;
    newBoard.addComment(this);

    return this;
  }

  public Comment writtenBy(Member writer) {
    if (Objects.nonNull(writer)) this.writer = writer;
    else throw new WriterUnmodifiableException();
    return this;
  }

  public Comment replyTo(Comment parentComment) {
    this.parentComment = parentComment;
    parentComment.addReply(this);

    return this;
  }

  private void addReply(Comment reply) {
    this.childrenComment.add(reply);
  }

  public void updateIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  /* others */

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(Comment.class.isAssignableFrom(o.getClass()))) return false;
    Comment comment = (Comment) o;
    return getId().equals(comment.getId())
        && getWriter().equals(comment.getWriter())
        && getContent().equals(comment.getContent())
        && getParentBoard().equals(comment.getParentBoard())
        && Objects.equals(getParentComment(), comment.getParentComment())
        && Objects.equals(getChildrenComment(), comment.getChildrenComment());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getId(),
        getWriter(),
        getContent(),
        getParentBoard(),
        getParentComment(),
        getChildrenComment());
  }

  public boolean isWrittenBy(Long writerId) {
    return writer.isSameMember(writerId);
  }
}
