package com.inhabas.api.domain.menu.repository;

import com.inhabas.api.domain.menu.domain.Menu;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer>, MenuRepositoryCustom {
  Optional<Menu> findByName_Value(String clubActivityMenuName);
}
