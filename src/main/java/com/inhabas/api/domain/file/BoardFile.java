package com.inhabas.api.domain.file;

import com.inhabas.api.domain.board.BaseBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "board_file")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor @Getter
public class BoardFile extends BaseFile {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_file_to_baseboard"))
    private BaseBoard parentBoard;

    // boardFile 과 baseBoard 의 연관관계 편의 메소드
    public void setParentBoard(BaseBoard newParentBoard) {
        // 기존의 file-board 연관관계를 끊는다.
        if (Objects.nonNull(this.parentBoard)) {
            this.parentBoard.getFiles().remove(this);
        }
        this.parentBoard = newParentBoard;
        parentBoard.addFile(this);
    }
}
