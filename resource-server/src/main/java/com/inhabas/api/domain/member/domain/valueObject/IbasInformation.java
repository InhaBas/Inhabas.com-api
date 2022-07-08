package com.inhabas.api.domain.member.domain.valueObject;

import com.inhabas.api.domain.team.domain.MemberTeam;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MemberTeam> teamList = new ArrayList<>();

    @Column(name = "joined")
    private LocalDateTime joinedDate;

    @Embedded
    private Introduce introduce;

    @Column(name = "USER_APPLY_PUBLISH", nullable = false)
    private Integer applyPublish = 0;

    public IbasInformation(Role role) {
        this.role = role;
        this.introduce = new Introduce();
        this.applyPublish = 0;
    }

    public String getIntroduce() {
        return introduce.getValue();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void addTeam(MemberTeam team) {
        this.teamList.add(team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IbasInformation)) return false;
        IbasInformation that = (IbasInformation) o;
        return getRole() == that.getRole()
                && getJoinedDate().equals(that.getJoinedDate())
                && getIntroduce().equals(that.getIntroduce())
                && getApplyPublish().equals(that.getApplyPublish());
    }

    public boolean isCompleteToSignUp() {
        return Objects.nonNull(this.joinedDate);
    }

    public void finishSignUp() {
        this.joinedDate = LocalDateTime.now();
    }
}