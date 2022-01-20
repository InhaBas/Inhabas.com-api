package com.inhabas.api.domain.board;

import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "normal_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("Normal")
public class NormalBoard extends BaseBoard {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "normal_board_category_fk"), updatable = false)
    private Category category;

    public NormalBoard(Integer id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    public NormalBoard(String title, String contents) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    @Override
    public NormalBoard writtenBy(Member writer) {
        return (NormalBoard) super.writtenBy(writer);
    }

    public NormalBoard inCategoryOf(Category category) {
        if (Objects.isNull(this.category))
            this.category = category;
        else
            throw new IllegalArgumentException("카테고리는 변경할 수 없습니다.");

        return this;
    }
}
