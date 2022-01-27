package com.inhabas.api.domain.menu;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.menu.wrapper.Description;
import com.inhabas.api.domain.menu.wrapper.MenuName;
import lombok.AccessLevel;
import lombok.Builder;
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
    @Column(name = "board_type")
    private MenuType type;

    @Embedded
    private MenuName name;

    @Embedded
    private Description description;

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public Menu(MenuGroup menuGroup, Integer priority, MenuType type, String name, String description) {
        this.menuGroup = menuGroup;
        this.priority = priority;
        this.type = type;
        this.name = new MenuName(name);
        this.description = new Description(description);
    }

    @Builder
    public Menu(Integer id, MenuGroup menuGroup, Integer priority, MenuType type, String name, String description) {
        this.id = id;
        this.menuGroup = menuGroup;
        this.priority = priority;
        this.type = type;
        this.name = new MenuName(name);
        this.description = new Description(description);
    }
}
