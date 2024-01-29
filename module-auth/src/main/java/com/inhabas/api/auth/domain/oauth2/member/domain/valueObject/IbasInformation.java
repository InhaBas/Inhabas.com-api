package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IbasInformation {
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "DATE_JOINED")
  private LocalDateTime dateJoined;

  @Embedded private Introduce introduce;

  @Column(name = "IS_HOF")
  private Boolean isHOF = false;

  public IbasInformation(Role role) {
    this.role = role;
    this.introduce = new Introduce();
    this.isHOF = false;
  }

  public String getIntroduce() {
    return introduce.getValue();
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setIsHOF(Boolean isHOF) {
    this.isHOF = isHOF;
  }

  public void setIntroduce(String introduce) {
    this.introduce = new Introduce(introduce);
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
