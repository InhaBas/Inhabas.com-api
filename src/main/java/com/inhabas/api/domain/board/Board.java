package com.inhabas.api.domain.board;

import com.inhabas.api.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@Getter @Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String contents;

    @ManyToOne
    private Member writer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Enumerated(EnumType.STRING)
    private Category category;


    public Board() {
    }

    public Board(String title, String contents, Member writer) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public Board(String title, String contents, Member writer, Category category) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.category = category;
    }
}
