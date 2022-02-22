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
        uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = { "group_id", "priority" })},
        indexes = {@Index(name = "menu_group_index", columnList = "group_id ASC"),
                @Index(name = "priority_index", columnList = "priority ASC")})
public class Menu extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "menu_group_id_fk"))
    private MenuGroup menuGroup;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_type")
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

    @Builder
    public Menu(MenuGroup menuGroup, Integer priority, MenuType type, String name, String description) {
        this.menuGroup = menuGroup;
        this.priority = priority;
        this.type = type;
        this.name = new MenuName(name);
        this.description = new Description(description);
    }
}
