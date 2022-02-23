package com.inhabas.api.controller;

import com.inhabas.api.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class FrontBoardController implements BoardController<Object, Object>{

    private final MenuService menuService;

    @Override
    public Object getBoard(Integer menuId, Integer id) {
        return menuService.findControllerByMenuId(menuId).get().getBoard(menuId, id);
    }

    @Override
    public Page<Object> getBoardList(Integer menuId, Pageable pageable) {
        return menuService.findControllerByMenuId(menuId).get().getBoardList(menuId, pageable);
    }

    @Override
    public void deleteBoard(Integer menuId, Integer id) {
        menuService.findControllerByMenuId(menuId).get().deleteBoard(menuId, id);
    }

    @Override
    public Integer updateBoard(Integer menuId, Map<String, Object> updateDto) {
        return menuService.findControllerByMenuId(menuId).get().updateBoard(menuId, updateDto);
    }

    @Override
    public Integer addBoard(Integer menuId, Map<String, Object> saveDto) {
        return menuService.findControllerByMenuId(menuId).get().addBoard(menuId, saveDto);
    }
}
