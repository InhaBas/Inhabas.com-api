package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Introduce;
import com.inhabas.api.domain.member.type.wrapper.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IbasInformation {
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime joined;

    @Column(name = "USER_INTRO")
    private Introduce introduce;

    @Column(name = "USER_APPLY_PUBLISH")
    private Integer applyPublish;

    public IbasInformation(Role role, String introduce, Integer applyPublish) {
        this.role = role;
        this.introduce = new Introduce(introduce);
        this.applyPublish = applyPublish;
    }

    public String getIntroduce() {
        return introduce.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IbasInformation)) return false;
        IbasInformation that = (IbasInformation) o;
        return getRole() == that.getRole()
                && getJoined().equals(that.getJoined())
                && getIntroduce().equals(that.getIntroduce())
                && getApplyPublish().equals(that.getApplyPublish());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getRole(),
                getJoined(),
                getIntroduce(),
                getApplyPublish());
    }

}