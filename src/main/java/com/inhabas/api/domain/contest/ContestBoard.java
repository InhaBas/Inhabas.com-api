package com.inhabas.api.domain.contest;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.contest.type.wrapper.Association;
import com.inhabas.api.domain.contest.type.wrapper.Topic;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.menu.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "contest_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContestBoard extends NormalBoard {

    @Embedded
    private Topic topic;

    @Embedded
    private Association association;

    @Column
    private LocalDate start;

    @Column
    private LocalDate deadline;

    /* Getter */
    public String getAssociation(){
        return association.getValue();
    }

    public String getTopic(){
        return topic.getValue();
    }

    /* Constructor */

    public ContestBoard(String title, String contents, String association, String topic, LocalDate start, LocalDate deadline){
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.association = new Association(association);
        this.topic = new Topic(topic);
        this.start = start;
        this.deadline = deadline;
    }

    @Builder
    public ContestBoard(Integer id, String title, String contents,String association, String topic, LocalDate start , LocalDate deadline){
        super(id, title, contents);
        this.association = new Association(association);
        this.topic = new Topic(topic);
        this.start = start;
        this.deadline =deadline;
    }

    /* relation method */


    public ContestBoard writtenBy(Member writer){
        super._writtenBy(writer);
        return this;
    }

    public ContestBoard inMenu(Menu menu){
        super._inMenu(menu);
        return this;
    }
}
