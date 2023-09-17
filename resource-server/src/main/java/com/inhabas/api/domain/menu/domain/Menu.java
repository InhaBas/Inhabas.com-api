package com.inhabas.api.domain.menu.domain;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.menu.domain.valueObject.Description;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuName;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MENU",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = { "MENU_GROUP_ID", "ORDER" })},
        indexes = {@Index(name = "MENU_GROUP_INDEX", columnList = "MENU_GROUP_ID ASC"),
                @Index(name = "ORDER_INDEX", columnList = "ORDER ASC")})
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MENU_GROUP_ID", foreignKey = @ForeignKey(name = "MENU_GROUP_ID_FK"))
    private MenuGroup menuGroup;

    private Integer order;

    @Embedded
    private MenuName name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false)
    private MenuType type;

    @Embedded
    private Description description;

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    @Builder
    public Menu(MenuGroup menuGroup, Integer order, MenuType type, String name, String description) {
        this.menuGroup = menuGroup;
        this.order = order;
        this.type = type;
        this.name = new MenuName(name);
        this.description = new Description(description);
    }


    public MenuId getId() {
        return new MenuId(this.id);
    }
}
