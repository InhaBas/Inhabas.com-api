package com.inhabas.api.domain.member.domain.valueObject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IbasInformation {
    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "DATE_JOINED", nullable = false)
    private LocalDateTime dateJoined;

    @Embedded
    private Introduce introduce;

    @Column(name = "IS_HOF", nullable = false)
    private int isHOF = 0;

    public IbasInformation(Role role) {
        this.role = role;
        this.introduce = new Introduce();
        this.isHOF = 0;
    }

    public String getIntroduce() {
        return introduce.getValue();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IbasInformation)) return false;
        IbasInformation that = (IbasInformation) o;
        return getRole() == that.getRole()
                && getDateJoined().equals(that.getDateJoined())
                && getIntroduce().equals(that.getIntroduce())
                && getIsHOF() == (that.getIsHOF());
    }

    public boolean isCompleteToSignUp() {
        return Objects.nonNull(this.dateJoined);
    }

    public void finishSignUp() {
        this.dateJoined = LocalDateTime.now();
    }
}