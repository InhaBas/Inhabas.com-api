package com.inhabas.api.domain.contest;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.contest.type.wrapper.Association;
import com.inhabas.api.domain.contest.type.wrapper.Topic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "content_board")
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

    public ContestBoard(Integer id, String title, String contents, String topic, String association, LocalDate start ,LocalDate deadline){
        super(id, title, contents);
        this.topic = new Topic(topic);
        this.association = new Association(association);
        this.start = start;
        this.deadline =deadline;
    }
}
