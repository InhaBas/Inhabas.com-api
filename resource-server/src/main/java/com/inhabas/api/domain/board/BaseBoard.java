package com.inhabas.api.domain.board;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@Getter
@DiscriminatorColumn(name = "TYPE", length = 15)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Embedded
    protected Title title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_BOARD_OF_USER_ID"))
    protected Member writer;


    public <T extends NormalBoard> T writtenBy(Member writer){

        if (Objects.isNull(this.writer)) {
            this.writer = writer;
            return (T) this;
        }
        else {
            throw new IllegalStateException("게시글 작성자를 수정할 수 없습니다.");
        }
    }

    public boolean isWriter(Member writer) {
        return this.writer.equals(writer);
    }

}
