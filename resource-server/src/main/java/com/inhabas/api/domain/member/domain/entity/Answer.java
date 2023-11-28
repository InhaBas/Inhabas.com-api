package com.inhabas.api.domain.member.domain.entity;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.questionaire.domain.Questionnaire;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ANSWER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ANSWER_OF_MEMBER"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QUESTIONNAIRE_ID", foreignKey = @ForeignKey(name = "FK_ANSWER_OF_QUESTION_ID"))
    private Questionnaire questionnaire;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    public Answer(Member member, Questionnaire questionnaire, String content) {
        this.member = member;
        this.questionnaire = questionnaire;
        this.content = content;
    }
}
