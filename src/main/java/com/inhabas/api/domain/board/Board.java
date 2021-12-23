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
@Table(name = "BOARD")
@Getter @Setter
public class Board {

    @Id
    @Column(name = "BOARD_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "BOARD_TITLE")
    private String title;

    @Column(
        name = "BOARD_CONT",
        columnDefinition = "MEDIUMTEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "BOARD_WRITER")
    private Member writer;

    @Column(name = "BOARD_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "BOARD_FIXDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Transient
    private Category category;
    private final static Map<Category, Integer> CATEGORY_MAP = Map.of(
            // this is for mapping DB table
            Category.notice, 1,
            Category.free, 2,
            Category.question, 3,
            Category.activity, 4,
            Category.alpha, 6,
            Category.beta, 7,
            Category.staff, 8,
            Category.suggest, 9
    );
    @Access(AccessType.PROPERTY)
    @Column(name = "BOARD_TYPE_NO")
    public Integer type(Category category) {
        return CATEGORY_MAP.get(category);
    }


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
