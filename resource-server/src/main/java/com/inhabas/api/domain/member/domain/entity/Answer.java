package com.inhabas.api.domain.member.domain.entity;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
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
    @Column(name = "NO")
    private Integer no;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_STUDENT_ID", foreignKey = @ForeignKey(name = "FK_ANSWER_OF_MEMBER"))
    private Member member;

    @Column(name = "QUESTION_NO", nullable = false)
    private Integer questionNo;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    public Answer(Member member, Integer questionNo, String content) {
        this.member = member;
        this.questionNo = questionNo;
        this.content = content;
    }
}
