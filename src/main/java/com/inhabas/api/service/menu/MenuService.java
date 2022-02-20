package com.inhabas.api.service.menu;

import com.inhabas.api.controller.BoardController;

import java.util.Optional;

public interface MenuService {

   Optional<BoardController> findControllerByMenuId(Integer menuId);

}
