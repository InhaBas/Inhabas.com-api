package com.inhabas.api.domain.board;

import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "normal_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("Normal")
public class NormalBoard extends BaseBoard {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "normal_board_category_fk"))
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
        this.category = category;
        return this;
    }
}
