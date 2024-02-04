package com.inhabas.api.domain.menu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer>, MenuRepositoryCustom {
  Optional<Menu> findByName_Value(String clubActivityMenuName);
}
