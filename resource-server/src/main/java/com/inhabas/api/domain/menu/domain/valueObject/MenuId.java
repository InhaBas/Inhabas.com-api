package com.inhabas.api.domain.menu.domain.valueObject;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuId implements Serializable {

  private static final long serialVersionUID = -7661257651938513762L;

  @JsonProperty("menuId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  public MenuId(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuId menuId = (MenuId) o;
    return id.equals(menuId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.valueOf(this.id);
  }
}
