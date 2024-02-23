package com.inhabas.api.domain.file.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.budget.domain.BudgetBoard;

@Entity
@Getter
@Table(name = "BUDGET_FILE")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetFile extends BaseFile {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "FK_FILE_OF_BUDGET"))
  private BudgetBoard board;

  @Builder
  public BudgetFile(String name, String url, BudgetBoard board) {
    super(name, url);
    this.board = board;
  }

  public <T extends BudgetBoard> void toBoard(T newParentBoard) {
    // 기존의 file-board 연관관계를 끊는다.
    if (Objects.nonNull(this.board)) {
      this.board.getReceipts().remove(this);
    }
    this.board = newParentBoard;
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
