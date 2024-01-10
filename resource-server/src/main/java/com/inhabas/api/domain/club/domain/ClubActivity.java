package com.inhabas.api.domain.club.domain;

import com.inhabas.api.domain.board.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.file.domain.BoardFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CLUB_ACTIVITY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("ALBUM")
public class ClubActivity extends BaseBoard {

    @Embedded
    private Content content;

    @OneToMany(mappedBy = "BaseBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<BoardFile> files = new HashSet<>();

    @Builder

    public ClubActivity(Title title, Content content, Set<BoardFile> files) {
        this.title = title;
        this.content = content;
        this.files = files;
    }
}
