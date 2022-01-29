package com.inhabas.api.domain.file;

import com.inhabas.api.domain.board.NormalBoard;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity @Getter
@Table(name = "board_file")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile extends BaseFile {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_file_to_baseboard"))
    private NormalBoard parentBoard;

    // boardFile 과 baseBoard 의 연관관계 편의 메소드
    public void toBoard(NormalBoard newParentBoard) {
        // 기존의 file-board 연관관계를 끊는다.
        if (Objects.nonNull(this.parentBoard)) {
            this.parentBoard.getFiles().remove(this);
        }
        this.parentBoard = newParentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(BoardFile.class.isAssignableFrom(o.getClass()))) return false;
        if (!super.equals(o)) return false;
        BoardFile boardFile = (BoardFile) o;
        return getParentBoard().getId().equals(boardFile.getParentBoard().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getParentBoard());
    }
}
