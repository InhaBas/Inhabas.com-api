package com.inhabas.api.domain.menu;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.menu.wrapper.MenuGroupName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private MenuGroupName name;

    public String getName() {
        return name.getValue();
    }

    public MenuGroup(String name) {
        this.name = new MenuGroupName(name);
    }

    public MenuGroup(Integer id, String name) {
        this.id = id;
        this.name = new MenuGroupName(name);
    }
}
