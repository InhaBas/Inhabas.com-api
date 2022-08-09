package com.inhabas.api.domain.board;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Embedded
    protected Title title;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "menu_id"))
    protected MenuId menuId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "writer_id"))
    protected MemberId writerId;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title.getValue();
    }

    public MenuId getMenuId() {
        return menuId;
    }

    public MemberId getWriterId() {
        return writerId;
    }


    @SuppressWarnings("unchecked")
    public <T extends NormalBoard> T writtenBy(MemberId writerId){

        if (Objects.isNull(this.writerId)) {
            this.writerId = writerId;
            return (T) this;
        }
        else {
            throw new IllegalStateException("게시글 작성자를 수정할 수 없습니다.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends NormalBoard> T inMenu(MenuId menuId) {
        this.menuId = menuId;
        return (T) this;
    }

    public boolean isWriter(MemberId writerId) {
        return this.writerId.equals(writerId);
    }
}
