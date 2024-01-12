package com.inhabas.api.domain.file.domain;

import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.NormalBoard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity @Getter
@Table(name = "BOARD_FILE")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile extends BaseFile {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_FILE_OF_BOARD"))
    private BaseBoard board;

    // boardFile 과 baseBoard 의 연관관계 편의 메소드

    @Builder
    public BoardFile(String name, String url, BaseBoard board) {
        super(name, url);
        this.board = board;
    }

    public void toBoard(NormalBoard newParentBoard) {
        // 기존의 file-board 연관관계를 끊는다.
        if (Objects.nonNull(this.board)) {
            this.board.getFiles().remove(this);
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
