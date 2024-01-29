package com.inhabas.api.domain.menu.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.menu.domain.valueObject.Description;
import com.inhabas.api.domain.menu.domain.valueObject.MenuName;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "MENU",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UniqueNumberAndStatus",
          columnNames = {"MENU_GROUP_ID", "PRIORITY"})
    },
    indexes = {
      @Index(name = "MENU_GROUP_INDEX", columnList = "MENU_GROUP_ID ASC"),
      @Index(name = "PRIORITY_INDEX", columnList = "PRIORITY ASC")
    })
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "MENU_GROUP_ID", foreignKey = @ForeignKey(name = "MENU_GROUP_ID_FK"))
  private MenuGroup menuGroup;

  @Column(name = "PRIORITY", nullable = false)
  private Integer priority;

  @Embedded private MenuName name;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private MenuType type;

  @Embedded private Description description;

  public String getName() {
    return name.getValue();
  }

  public String getDescription() {
    return description.getValue();
  }

  @Builder
  public Menu(
      MenuGroup menuGroup, Integer priority, MenuType type, String name, String description) {
    this.menuGroup = menuGroup;
    this.priority = priority;
    this.type = type;
    this.name = new MenuName(name);
    this.description = new Description(description);
  }
}
