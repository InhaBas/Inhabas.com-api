package com.inhabas.api.domain.menu.repository;

import com.inhabas.api.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer>, MenuRepositoryCustom {
    Optional<Menu> findByName_Value(String clubActivityMenuName);

}
