package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.board.exception.WriterUnmodifiableException;
import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.file.domain.BudgetFile;

@Getter
@Entity
@DiscriminatorColumn(name = "TYPE", length = 15)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BudgetBoard extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Embedded protected Title title;

  @Embedded protected Details details;

  @Column(nullable = false, columnDefinition = "DATETIME(0)")
  protected LocalDateTime dateUsed;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_BUDGET_BOARD_OF_USER_ID"))
  protected Member writer;

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
  protected List<BudgetFile> receipts = new ArrayList<>();

  public <T extends BudgetBoard> T writtenBy(Member writer, Class<T> boardClass) {

    if (Objects.isNull(this.writer)) {
      this.writer = writer;
      if (!boardClass.isInstance(this)) {
        throw new InvalidInputException();
      }
      return boardClass.cast(this);
    } else {
      throw new WriterUnmodifiableException();
    }
  }

  public boolean isWrittenBy(Member writer) {
    return this.writer.equals(writer);
  }

  public String getTitle() {
    return title.getValue();
  }

  public String getDetails() {
    return details.getValue();
  }

  public BudgetBoard(String title, String details, LocalDateTime dateUsed, Member writer) {
    this.title = new Title(title);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.writer = writer;
  }

  public void addReceipt(BudgetFile receipt) {
    if (this.receipts == null) {
      this.receipts = new ArrayList<>();
    }

    this.receipts.add(receipt);
  }

  public void updateReceipts(List<BudgetFile> receipts) {

    if (this.receipts != null) {
      this.receipts.clear();
    } else {
      this.receipts = new ArrayList<>();
    }

    for (BudgetFile receipt : receipts) {
      addReceipt(receipt);
    }
  }
}
