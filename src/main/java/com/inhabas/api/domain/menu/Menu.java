package com.inhabas.api.domain.menu;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.menu.wrapper.MenuName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = { "group_id", "priority" })})
public class Menu extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private MenuGroup menuGroup;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    private MenuType Type;

    @Embedded
    private MenuName name;

    public String getName() {
        return name.getValue();
    }

    public Menu(MenuGroup menuGroup, Integer priority, MenuType type, String name) {
        this.menuGroup = menuGroup;
        this.priority = priority;
        Type = type;
        this.name = new MenuName(name);
    }

    public Menu(Integer id, MenuGroup menuGroup, Integer priority, MenuType type, String name) {
        this.id = id;
        this.menuGroup = menuGroup;
        this.priority = priority;
        Type = type;
        this.name = new MenuName(name);
    }
}
