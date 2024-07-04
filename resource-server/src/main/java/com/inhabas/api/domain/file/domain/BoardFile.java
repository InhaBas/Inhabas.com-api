package com.inhabas.api.domain.file.domain;

import java.util.Objects;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.global.util.FileUtil;

@Entity
@Getter
@Table(name = "BOARD_FILE")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile extends BaseFile {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "FK_FILE_OF_BOARD"))
  private BaseBoard board;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_FILE"))
  private Member uploader;

  @Builder
  public BoardFile(String id, String name, String url, Member uploader, Long size, String type) {
    super(id, name, url, size, type);
    this.uploader = uploader;
  }

  // boardFile 과 baseBoard 의 연관관계 편의 메소드
  public <T extends BaseBoard> void toBoard(T newParentBoard) {
    if (Objects.isNull(this.board)) {
      this.board = newParentBoard;
    } else if (!this.board.getId().equals(newParentBoard.getId())) {
      throw new InvalidInputException();
    }
  }

  public BoardFile copyFileWithNewId() {
    return BoardFile.builder()
        .id(FileUtil.generateUUID())
        .name(this.name.getValue())
        .url(this.url.getValue())
        .size(this.size)
        .type(this.type)
        .uploader(this.uploader)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(BoardFile.class.isAssignableFrom(o.getClass()))) return false;
    if (!super.equals(o)) return false;
    BoardFile boardFile = (BoardFile) o;
    return getBoard().getId().equals(boardFile.getBoard().getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getBoard());
  }
}
