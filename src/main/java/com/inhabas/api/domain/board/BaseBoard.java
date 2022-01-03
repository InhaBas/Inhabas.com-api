package com.inhabas.api.domain.board;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.comment.Comment;
import com.inhabas.api.domain.file.BoardFile;
import com.inhabas.api.domain.member.Member;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "base_board")
@Getter
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", length = 15)
public class BaseBoard extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    protected String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    protected String contents;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_baseboard_to_user"))
    protected Member writer;

    @OneToMany(mappedBy = "parentBoard")
    protected List<Comment> comments;

    @OneToMany(mappedBy = "parentBoard")
    protected Set<BoardFile> files = new HashSet<>();

    public boolean isWriter(Member member) {
        return this.writer.equals(member);
    }

    public void addFile(BoardFile uploadFile) {
        this.getFiles().add(uploadFile);
    }

    public void addComment(Comment newComment) {
        this.getComments().add(newComment);
    }

}

