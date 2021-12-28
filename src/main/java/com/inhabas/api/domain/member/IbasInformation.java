package com.inhabas.api.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Embeddable
@Getter @ToString
@NoArgsConstructor
public class IbasInformation {
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime joined;

    @Column(name = "USER_INTRO")
    private String introduce;

    @Column(name = "USER_APPLY_PUBLISH")
    private Integer applyPublish;

    public IbasInformation(Role role, String introduce, Integer applyPublish) {
        this.role = role;
        this.introduce = introduce;
        this.applyPublish = applyPublish;
    }
}