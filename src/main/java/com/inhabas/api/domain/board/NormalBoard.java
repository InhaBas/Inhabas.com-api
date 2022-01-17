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

    @Enumerated(EnumType.STRING)
    private Category category;

    public NormalBoard(String title, String contents, Category category) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.category = category;
    }

    public NormalBoard(String title, String contents, Member writer, Category category) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.writer = writer;
        this.category = category;
    }

    public NormalBoard(Integer id, String title, String contents, Member writer, Category category) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.writer = writer;
        this.category = category;
    }

    @Override
    public NormalBoard writtenBy(Member writer) {
        return (NormalBoard) super.writtenBy(writer);
    }
}
