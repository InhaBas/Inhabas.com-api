package com.inhabas.api.domain.contest;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.contest.type.wrapper.Association;
import com.inhabas.api.domain.contest.type.wrapper.Topic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contest_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContestBoard extends NormalBoard {

    @Embedded
    protected Topic topic;

    @Embedded
    protected Association association;

    @Column
    protected LocalDate start;

    @Column
    protected LocalDate deadline;

    /* Constructor */

    public ContestBoard(String title, String contents, String association, String topic, LocalDate start, LocalDate deadline){
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.association = new Association(association);
        this.topic = new Topic(topic);
        this.start = start;
        this.deadline = deadline;
    }

    public ContestBoard(Integer id, String title, String contents,String association, String topic, LocalDate start , LocalDate deadline){
        super(id, title, contents);
        this.association = new Association(association);
        this.topic = new Topic(topic);
        this.start = start;
        this.deadline =deadline;
    }

    /* Setter */

    public void setTopic(String topic) {
        this.topic = new Topic(topic);
    }

    public void setAssociation(String association) {
        this.association = new Association(association);
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
