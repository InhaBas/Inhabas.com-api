package com.inhabas.api.domain.questionaire;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "question_form_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_question_form_answer_of_member"))
    private Member member;

    @Column(name = "question_no", nullable = false)
    private Integer questionNo;

    @Column(name = "answer", nullable = false, length = 1000)
    private String answer;

    public Answer(Member member, Integer questionNo, String answer) {
        this.member = member;
        this.questionNo = questionNo;
        this.answer = answer;
    }
}
