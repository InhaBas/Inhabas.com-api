package com.inhabas.api.domain.board;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member writer;

    @Enumerated(EnumType.STRING)
    private Category category;

    public Board() {}

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

    public Board(Integer id, String title, String contents, Member writer, Category category) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.category = category;
    }
}
