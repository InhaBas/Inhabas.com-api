package com.inhabas.api.domain.club.domain.entity;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CLUB_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_CLUB_HISTORY"))
    private Member member;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Column(name = "DATE_HISTORY", nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime dateHistory;

    @CreatedDate
    @Column(name = "DATE_CREATED", nullable = false, updatable = false, insertable = false, columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated;

    public ClubHistory(Member member, Title title, Content content, LocalDateTime dateHistory) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.dateHistory = dateHistory;
    }

}
