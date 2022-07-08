package com.inhabas.api.domain.contest.domain;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.valueObject.Contents;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.contest.domain.valueObject.Association;
import com.inhabas.api.domain.contest.domain.valueObject.Topic;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

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

}
