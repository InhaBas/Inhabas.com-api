package com.inhabas.api.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer>, MenuRepositoryCustom {
  Menu findByName_Value(String clubActivityMenuName);
}
